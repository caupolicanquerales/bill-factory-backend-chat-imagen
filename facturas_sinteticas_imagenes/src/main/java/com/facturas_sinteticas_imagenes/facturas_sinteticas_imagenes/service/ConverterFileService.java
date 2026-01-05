package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ConverterFileService {
	
	public  Map<String, byte[]> convertFileToMap(List<MultipartFile> fileParts){
		return  fileParts.stream()
		        .collect(Collectors.toMap(
		            MultipartFile::getOriginalFilename, 
		            this::convertFileToByteArray,
		            (existing, replacement) -> existing ));
	} 
	
	private byte[] convertFileToByteArray(MultipartFile file){
		try {
			return file.getBytes();
		} catch (IOException e) {
			throw new RuntimeException("Failed to convert File to byte array.", e);
		}
	}
}
