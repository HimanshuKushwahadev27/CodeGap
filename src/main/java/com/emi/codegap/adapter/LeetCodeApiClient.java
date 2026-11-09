package com.emi.codegap.adapter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class LeetCodeApiClient implements PlatformAdapter {

    private static final String RECENT_SUBMISSIONS_QUERY = """
        query recentSubmissions($username: String!, $limit: Int!) {
          recentAcSubmissionList(username: $username, limit: $limit) {
            title titleSlug timestamp
          }
        }
        """;

    private final RestClient client;

    public LeetCodeApiClient(RestClient.Builder builder) {
        this.client = builder.baseUrl("https://leetcode.com").build();
    }

    @Override
    public List<PlatformSubmission> fetchSubmissions(String handle) {
        JsonNode response = client.post()
            .uri("/graphql")
            .body(java.util.Map.of(
                "query", RECENT_SUBMISSIONS_QUERY,
                "variables", java.util.Map.of("username", handle, "limit", 100)
            ))
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, responseMessage) ->
                new IllegalStateException("LeetCode API returned " + responseMessage.getStatusCode()))
            .body(JsonNode.class);

        if (response == null || response.has("errors")) {
            throw new IllegalStateException("LeetCode API returned an unsuccessful response");
        }

        return java.util.stream.StreamSupport.stream(
                response.path("data").path("recentAcSubmissionList").spliterator(), false)
            .map(submission -> new PlatformSubmission(
                submission.path("titleSlug").asText(),
                submission.path("title").asText("Unknown problem"),
                null,
                new HashSet<>(),
                Instant.ofEpochSecond(submission.path("timestamp").asLong())
            ))
            .toList();
    }
}
