/**
 * 
 */
package com.csulb.set.homeworks.hw2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import com.csulb.cecs529.set.homeworks.Constants;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

/**
 * @author Manav
 *
 */
public class JsonParsing {	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
				.create();
		
		Path jsonFile = Paths.get(Constants.ROOT_DIRECTORY, "\\Homeworks\\hw2\\all-nps-sites.json");		
		Path outputDirectory = Paths.get(Constants.ROOT_DIRECTORY, "\\Homeworks\\hw2\\jsonFiles");
		
		System.out.println(outputDirectory.toString());

		
		//FileWriter writer = null;
		String fileName = "\\article";
		
		Reader reader = null;
		try {
			reader = new FileReader(jsonFile.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JsonParser jsonParser = new JsonParser();
		JsonElement el = jsonParser.parse(reader);
		
		if (el.isJsonObject()) {
			JsonObject docs = el.getAsJsonObject();			
			if (docs.get("documents").isJsonArray()) {
				JsonArray docsArray = docs.get("documents").getAsJsonArray();
				AtomicInteger i = new AtomicInteger(0);
				docsArray.forEach(element -> {
					try {
						JsonWriter writer = new JsonWriter(new FileWriter(outputDirectory + fileName + i.incrementAndGet() + ".json"));
						gson.toJson(element, writer);
						writer.flush();
						writer.close();
					} catch (Exception e) {
						e.printStackTrace();
					}					
				});
			}
		}

	}

}
