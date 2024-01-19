package com.example.demo.ExpenseManagement.Service.DozerBeanMapper;

import java.time.ZonedDateTime;

import org.dozer.DozerConverter;

public class ZonedDateTimeConverter extends DozerConverter<ZonedDateTime, ZonedDateTime> {

	public ZonedDateTimeConverter() {
		super(ZonedDateTime.class, ZonedDateTime.class);
	}

	@Override
	public ZonedDateTime convertFrom(ZonedDateTime src, ZonedDateTime dst) {

		if ( src == null)
		{
			return null;
		}

		return ZonedDateTime.from(src);
	}

	@Override
	public ZonedDateTime convertTo(ZonedDateTime src, ZonedDateTime dst) {

		if ( src == null)
		{
			return null;
		}

		return ZonedDateTime.from(src);
	}

}