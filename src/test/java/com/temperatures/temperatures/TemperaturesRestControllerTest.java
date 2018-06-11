package com.temperatures.temperatures;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
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

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.temperatureRepository.deleteAllInBatch();

        //this.temperature = temperatureRepository.save(new Temperature("60F", new Date(2018,6,10), new Date(2018,6,10)));
        this.tempList.add(temperatureRepository.save(new Temperature(temp, new Date(2018,6,10), new Date(2018,6,10))));
        this.tempList.add(temperatureRepository.save(new Temperature("60F", new Date(2018,6,10), new Date(2018,6,10))));
    }

    @Test
    public void tempNotFound() throws Exception {
        mockMvc.perform(put("/")
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
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.tempList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].createDate", is(this.tempList.get(0).getCreateDate().toString())))
                .andExpect(jsonPath("$[0].updateDate", is(this.tempList.get(0).getUpdateDate().toString())))
                .andExpect(jsonPath("$[1].id", is(this.tempList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].createDate", is(this.tempList.get(0).getCreateDate().toString())))
                .andExpect(jsonPath("$[1].updateDate", is(this.tempList.get(0).getUpdateDate().toString())));
    }

    @Test
    public void createTemperature() throws Exception {
        String temperatureJson = json(new Temperature(
                this.temp, new Date(2018,6,10), new Date(2018,6,10)));

        this.mockMvc.perform(post("/")
                .contentType(contentType)
                .content(temperatureJson))
                .andExpect(status().isCreated());
    }
    
    @Test
    public void replaceTemperature() throws Exception {
        String temperatureJson = json(new Temperature(
                this.temp, new Date(2018,6,10), new Date(2018,6,10)));

        this.mockMvc.perform(post("/")
                .contentType(contentType)
                .content(temperatureJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
