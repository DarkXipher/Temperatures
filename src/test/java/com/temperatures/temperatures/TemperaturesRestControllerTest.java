package com.temperatures.temperatures;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TemperaturesApplication.class)
@WebAppConfiguration
public class TemperaturesRestControllerTest
{
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    //private String userName = "bdussault";
    private String temp = "34C";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Temperature temperature;

    private List<Temperature> tempList = new ArrayList<Temperature>();

    private static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss aa zzz yyyy");
    
    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @BeforeClass
    public static void setupClass() throws Exception {
    	sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.temperatureRepository.deleteAllInBatch();

        this.tempList.add(temperatureRepository.save(new Temperature(temp, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()))));
        this.tempList.add(temperatureRepository.save(new Temperature("60F", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()))));
        this.temperature = temperatureRepository.save(new Temperature("24", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
    }
    
    @Test
    public void tempNotFoundPut() throws Exception {
        mockMvc.perform(put("/99999")
                .content(this.json(new Temperature(null, null, null)))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }
    @Test
    public void tempNotFoundDelete() throws Exception {
        mockMvc.perform(delete("/99999")
                .content(this.json(new Temperature(null, null, null)))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }
/*
    @Test
    public void readSingleBookmark() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks/"
                + this.bookmarkList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.bookmarkList.get(0).getId().intValue())))
                .andExpect(jsonPath("$.uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$.description", is("A description")));
    }
*/
    @Test
    public void readTemperatures() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(this.tempList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].createDate", is(sdf.format(this.tempList.get(0).getCreateDate()))))
                .andExpect(jsonPath("$[0].updateDate", is(sdf.format(this.tempList.get(0).getUpdateDate()))))
                .andExpect(jsonPath("$[1].id", is(this.tempList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].createDate", is(sdf.format(this.tempList.get(0).getCreateDate()))))
                .andExpect(jsonPath("$[1].updateDate", is(sdf.format(this.tempList.get(0).getUpdateDate()))));
    }

    @Test
    public void createTemperature() throws Exception {
        String temperatureJson = json(new Temperature(
                this.temp, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));

        this.mockMvc.perform(post("/")
                .contentType(contentType)
                .content(temperatureJson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void replaceTemperature() throws Exception {
        String temperatureJson = json(new Temperature(
                this.temp, new Date(System.currentTimeMillis()-(10*60*60*60*24)), new Date(System.currentTimeMillis()-(10*60*60*60*24))));

        this.mockMvc.perform(put("/" + this.temperature.getId().intValue())
                .contentType(contentType)
                .content(temperatureJson))
                .andExpect(status().isOk());
    }
    /**
     * ID is auto generated and thus hard to determine which ID needs to be removed.
     *
     * @throws Exception
     */
    /*
    @Test
    public void deleteTemperature() throws Exception {
        this.mockMvc.perform(delete("/1")
                .contentType(contentType))
                .andExpect(status().isNoContent());
    }
    */

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
