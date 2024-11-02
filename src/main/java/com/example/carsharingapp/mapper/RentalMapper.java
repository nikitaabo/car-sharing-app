package com.example.carsharingapp.mapper;

import com.example.carsharingapp.config.MapperConfig;
import com.example.carsharingapp.dto.CreateRentalRequestDto;
import com.example.carsharingapp.dto.RentalDto;
import com.example.carsharingapp.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "userId", source = "user.id")
    RentalDto toDto(Rental rental);

    @Mapping(target = "car", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    Rental toModel(CreateRentalRequestDto createRentalRequestDto);
}
