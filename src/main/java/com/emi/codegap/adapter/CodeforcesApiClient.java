package com.emi.codegap.adapter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class CodeforcesApiClient implements PlatformAdapter {

    private final RestClient client;

    public CodeforcesApiClient(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://codeforces.com").build();
    }

    @Override
    public List<PlatformSubmission> fetchSubmissions(String handle) {
        JsonNode response = client.get()
            .uri(uri -> uri.path("/api/user.status").queryParam("handle", handle).build())
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, responseMessage) ->
                new IllegalStateException("Codeforces API returned " + responseMessage.getStatusCode()))
            .body(JsonNode.class);

        if (response == null || !"OK".equals(response.path("status").asText())) {
            throw new IllegalStateException("Codeforces API returned an unsuccessful response");
        }

        return java.util.stream.StreamSupport.stream(response.path("result").spliterator(), false)
            .filter(submission -> submission.path("verdict").asText().equals("OK"))
            .map(submission -> {
                JsonNode problem = submission.path("problem");
                return new PlatformSubmission(
                    problem.path("contestId").asText() + "-" + problem.path("index").asText(),
                    problem.path("name").asText("Unknown problem"),
                    problem.has("rating") ? problem.path("rating").asText() : null,
                    tags(problem.path("tags")),
                    Instant.ofEpochSecond(submission.path("creationTimeSeconds").asLong())
                );
            })
            .toList();
    }

    private HashSet<String> tags(JsonNode tags) {
        HashSet<String> result = new HashSet<>();
        tags.forEach(tag -> result.add(tag.asText()));
        return result;
    }
}
