package com.temperatures.temperatures;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Temperature
{
	@Id
    @GeneratedValue
    private Long id;

    private String temperature;
    
    private Date createDate;
    
    private Date updateDate;

    @SuppressWarnings("unused")
	private Temperature() { } // JPA only

    public Temperature(final String temp, final Date create, final Date update) {

        this.temperature = temp;
        this.createDate = create;
        this.updateDate = update;
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

	public Date getCreateDate()
	{
		return createDate;
	}

	public Date getUpdateDate()
	{
		return updateDate;
	}

}
