package com.facturas_sinteticas_imagenes.facturas_sinteticas_imagenes.request;

import java.util.List;

public class GenerationImageRequest {
	
	private List<String> prompt;

	public List<String> getPrompt() {
		return prompt;
	}

	public void setPrompt(List<String> prompt) {
		this.prompt = prompt;
	}
	
	
}
