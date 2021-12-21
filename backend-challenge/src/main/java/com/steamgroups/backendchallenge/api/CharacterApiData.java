package com.steamgroups.backendchallenge.api;

import com.google.gson.Gson;
import com.steamgroups.backendchallenge.model.Character;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.List;


public class CharacterApiData {

    public static List<Character> fetchCharacters() {
        String webService = "https://swapi.dev/api/people/";

        Client c = Client.create();
        WebResource wr;
        String json = null;
        List<Character> characters = new ArrayList<>();

        try {
            for (int i = 1; i <= 82; i++) {
                wr = c.resource(webService + i + "?format=json");
                json = fetchCharacter(wr);
                Gson gson = new Gson();
                characters.add(gson.fromJson(json, Character.class));
            }
        } catch (RuntimeException e) {
            System.out.println(e);
        }

        return characters;
    }

    private static String fetchCharacter(WebResource wr) {
        try {
            return wr.get(String.class);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
        return null;
    }
}
