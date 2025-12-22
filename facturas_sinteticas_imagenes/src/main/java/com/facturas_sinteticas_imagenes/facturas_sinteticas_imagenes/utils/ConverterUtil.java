package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.utils;

import org.springframework.http.codec.ServerSentEvent;

import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.DataMessage;
import com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.response.GenerationSyntheticDataResponse;


public class ConverterUtil {
	
	public static GenerationSyntheticDataResponse getGenerationSyntheticDataResponse(String message) {
		GenerationSyntheticDataResponse response= new GenerationSyntheticDataResponse();
		response.setResponse(message);
		return response;
	} 
	
	public static DataMessage setDataMessage(String data) {
		DataMessage dataMessage= new DataMessage();
		dataMessage.setMessage(data);
		return dataMessage;
	}
	
	public static ServerSentEvent<DataMessage> setServerSentEvent(DataMessage data, String eventName) {
		return ServerSentEvent.<DataMessage>builder()
				.id("1")
				.comment("prueba")
				.event(eventName)
			    .data(data)
			    .build();
	}
}
