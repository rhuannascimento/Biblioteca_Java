package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Reserva;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {

    private long id;
    private long idTomador;
    private long idObra;

    public static ReservaDTO create(Reserva reserva) {
        ModelMapper modelMapper = new ModelMapper();
        ReservaDTO dto = modelMapper.map(reserva, ReservaDTO.class);;
        dto.idTomador = reserva.getTomador().getId();
        dto.idObra = reserva.getObra().getId();
        return dto;
    }

}
