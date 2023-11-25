package com.example.servingwebcontent.controller;

import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.example.servingwebcontent.entity.CountryEntity;
import com.example.servingwebcontent.form.CountryForm;
import com.example.servingwebcontent.form.CountrySearchForm;
import com.example.servingwebcontent.repository.CountryEntityMapper;
import com.google.gson.Gson;

@Controller
public class CountryController {

	@Autowired
	private CountryEntityMapper mapper;

	@GetMapping("/country")
	public String init(CountrySearchForm countrySearchForm) {

		return "country/country";
	}

	/**
	 * Represents a sequence of characters. In this context, it is used to return a
	 * JSON representation of a CountryEntity object.
	 */
	@PostMapping("/country/getCountry")
	@ResponseBody
	public String getCountry(@Validated CountrySearchForm countrySearchForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		/**
		 * Optional object containing the result of the database query for the country
		 * with the specified country code.
		 */
		Optional<CountryEntity> countryEntity = mapper.selectByPrimaryKey(countrySearchForm.getMstCountryCD());

		// return code should not be 404
		return new Gson().toJson(countryEntity.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}

	@PostMapping("/addCountry")
	@ResponseBody
	public String addCountry(@Validated CountryForm countryForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return "list";
        }
		CountryEntity countryEntity = new CountryEntity();
		BeanUtils.copyProperties(countryForm, countryEntity);
		
		// set UpdateDt
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
		String updateDtStr = sdf.format(System.currentTimeMillis());
		long updateDtLong = Long.parseLong(updateDtStr);
		countryEntity.setUpdatedt(updateDtLong);
		
		// insert countryEntity
		Integer count = mapper.insert(countryEntity);
		return count.toString();
	}
	
	@PostMapping("/deleteCountry")
	@ResponseBody
	public String deleteCountry(@Validated CountryForm countryForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return "list";
        }
		// delete countryEntity
		// physics delete
		Integer count = mapper.deleteByPrimaryKey(countryForm.getMstcountrycd());
		return count.toString();
	}
	
	@PostMapping("/updateCountry")
	@ResponseBody
	public String updateCountry(@Validated CountryForm countryForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return "list";
        }
		CountryEntity countryEntity = new CountryEntity();
		BeanUtils.copyProperties(countryForm, countryEntity);
		
		// set UpdateDt
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyyMMdd");
		String updateDtStr = sdf.format(System.currentTimeMillis());
		long updateDtLong = Long.parseLong(updateDtStr);
		countryEntity.setUpdatedt(updateDtLong);
		
		// update countryEntity
		Integer count = mapper.updateByPrimaryKeySelective(countryEntity);
		return count.toString();
	}

}
