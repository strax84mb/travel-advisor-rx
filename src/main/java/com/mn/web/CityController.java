package com.mn.web;

import com.mn.dtos.CityResponse;
import com.mn.dtos.WriteCityRequest;
import com.mn.exceptions.CityNotFoundException;
import com.mn.services.CityService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;

@Controller("/city")
public class CityController {

    private CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public CityResponse addCity(@Body WriteCityRequest dto) {
        return cityService.save(dto).blockingGet();
    }

    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured("ADMIN")
    public CityResponse updateCity(@PathVariable("id") Long id, @Body WriteCityRequest dto) {
        var city = cityService.update(id, dto).blockingGet();
        if (city == null) {
            throw new CityNotFoundException(id);
        } else {
            return city;
        }
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public CityResponse getCity(@PathVariable("id") Long id,
                                @QueryValue(value = "max-comments", defaultValue = "-1") int maxComments) {
        var max = maxComments < 0 ? Integer.MAX_VALUE : maxComments;
        var city = cityService.getById(id, max).blockingGet();
        if (city == null) {
            throw new CityNotFoundException(id);
        } else {
            return city;
        }
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    public List<CityResponse> listAllCities(@QueryValue(value = "max-comments", defaultValue = "-1") int maxComments) {
        var max = maxComments < 0 ? Integer.MAX_VALUE : maxComments;
        return cityService.getAll(max).toList().blockingGet();
    }

    @Delete("/{id}")
    @Secured("ADMIN")
    public boolean deleteCity(@PathVariable("id") Long id) {
        return cityService.delete(id);
    }
}
