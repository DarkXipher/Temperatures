package com.temperatures.temperatures;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Temperature
{
	@Id
    @GeneratedValue
    private Long id;

    private String temperature;
    
    private String scale = "Celsius";
    
    @JsonFormat(pattern="EEE MMM dd hh:mm:ss aa zzz yyyy")
    private Date createDate;
    
    @JsonFormat(pattern="EEE MMM dd hh:mm:ss aa zzz yyyy")
    private Date updateDate;

    @SuppressWarnings("unused")
	private Temperature() { } // JPA only

    public Temperature(final String temp, final Date create, final Date update) {

        this.temperature = temp;
        this.createDate = create;
        this.updateDate = update;
    }
    
    public Temperature(final String temp, final Date create, final Date update, final String scale) {
    	this.temperature = temp;
        this.createDate = create;
        this.updateDate = update;
        this.scale = scale;
    }
    
    public Temperature(final Long id, final String temp, final Date create, final Date update, final String scale) {
    	this.id = id;
    	this.temperature = temp;
        this.createDate = create;
        this.updateDate = update;
        this.scale = scale;
    }

    /*
    public static Temperature from(Temperature temp) {
        return new Temperature(temp.temperature, temp.createDate, temp.updateDate);
    }
*/
    public Long getId() {
        return id;
    }

	public String getTemperature()
	{
		return temperature;
	}

	public String getScale()
	{
		return scale;
	}
	
	public Date getCreateDate()
	{
		return createDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

}
