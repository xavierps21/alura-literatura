package com.alura.literatura;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GutendexRequestBuilder {
    private static final String BASE_URL = "https://gutendex.com/books/";
    private final HashMap<String, String> queryParams = new HashMap<>();

    public GutendexRequestBuilder authorYearStart(int authorYearStart) {
        queryParams.put("author_year_start", String.valueOf(authorYearStart));
        return this;
    }

    public GutendexRequestBuilder authorYearEnd(int authorYearEnd) {
        queryParams.put("author_year_end", String.valueOf(authorYearEnd));
        return this;
    }

    public GutendexRequestBuilder copyrights(Collection<Boolean> copyrights) {
        queryParams.put("copyright", copyrights.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return this;
    }

    public GutendexRequestBuilder ids(Collection<Integer> ids) {
        queryParams.put("ids", ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return this;
    }

    public GutendexRequestBuilder languages(Collection<String> languages) {
        queryParams.put("languages", String.join(",", languages));
        return this;
    }

    public GutendexRequestBuilder mimeType(String mimeType) {
        queryParams.put("mime_type", mimeType);
        return this;
    }

    public GutendexRequestBuilder search(String search) {
        queryParams.put("search", search);
        return this;
    }

    public GutendexRequestBuilder sort(String sort) {
        queryParams.put("sort", sort);
        return this;
    }

    public GutendexRequestBuilder topic(String topic) {
        queryParams.put("topic", topic);
        return this;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode query parameter value: " + value, e);
        }
    }

    public Iterable<GutendexBook> execute() {
        HttpClient httpClient = HttpClient.newHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String queryString = queryParams
                .entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + encodeValue(entry.getValue()))
                .collect(Collectors.joining("&"));
        ArrayList<GutendexBook> books = new ArrayList<GutendexBook>();

        return () -> new Iterator<GutendexBook>() {
            private String url = BASE_URL + "?" + queryString;
            private GutendexBook book = null;

            @Override
            public boolean hasNext() {
                if (!books.isEmpty()) {
                    book = books.remove(books.size() - 1);
                    return true;
                }

                if (url != null) {
                    try {
                        HttpRequest httpRequest = HttpRequest.newBuilder()
                                .uri(URI.create(url))
                                .GET()
                                .build();
                        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                        GutendexResponse gutendexResponse = objectMapper.readValue(httpResponse.body(), GutendexResponse.class);

                        books.addAll(gutendexResponse.getResults());

                        url = gutendexResponse.getNext();
                    } catch (Exception e) {
                        // skip exception
                    }
                }

                if (!books.isEmpty()) {
                    book = books.remove(books.size() - 1);
                    return true;
                }

                return false;
            }

            @Override
            public GutendexBook next() {
                return book;
            }
        };
    }
}
