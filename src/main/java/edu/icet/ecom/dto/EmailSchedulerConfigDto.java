package edu.icet.ecom.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailSchedulerConfigDto {

    private Integer id;

    @JsonDeserialize(using = FlexibleLocalTimeDeserializer.class)
    @JsonSerialize(using = FlexibleLocalTimeSerializer.class)
    @NotNull(message = "sendTime is required in HH:mm or HH:mm:ss format")
    private LocalTime sendTime;
}


