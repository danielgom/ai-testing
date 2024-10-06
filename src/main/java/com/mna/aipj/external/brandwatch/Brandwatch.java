package com.mna.aipj.external.brandwatch;

import com.mna.aipj.dto.exception.UserException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class Brandwatch {

    @Value("${brandwatch.host}")
    private String host;

    @Value("${brandwatch.username}")
    private String username;

    @Value("${brandwatch.password}")
    private String password;

    @Value("${brandwatch.projectID}")
    private String projectID;

    private String accessToken;

    private LocalDateTime accessTokenExpiresAt;

    private RestClient restClient;

    private static final Logger logger = LoggerFactory.getLogger(Brandwatch.class);

    public BrandwatchMentionResponse getInitialQueryMentions(String queryID) {
        return getMentions(queryID, null);
    }

    public BrandwatchMentionResponse getQueryMentionsAddedSince(String queryID, LocalDateTime addedSince) {
        return getMentions(queryID, addedSince);
    }

    private BrandwatchMentionResponse getMentions(String queryID, LocalDateTime addedSince) {
        logger.info("getting projectID ({}) queryID({}) mentions...", this.projectID, queryID);
        this.login();
        String mentionsURL = String.format("/projects/%s/data/mentions",
                this.projectID);

        int pageSize = 300; // retrieves last x amount of mentions
        LocalDate currentDate = LocalDate.now();

        try {
            BrandwatchMentionResponse mentions = this.getRestClient().get()
                    .uri(uriBuilder -> {
                        UriBuilder requestBuilder = uriBuilder.path(mentionsURL)
                                .queryParam("queryId", queryID)
                                .queryParam("startDate", currentDate.minusDays(30))
                                .queryParam("endDate", currentDate)
                                .queryParam("pageSize", pageSize)
                                .queryParam("page", 0)
                                .queryParam("orderBy", "added")
                                .queryParam("orderDirection", "desc");

                        if (addedSince != null) {
                            logger.info("retrieving new mentions added since ({})...", addedSince);
                            requestBuilder.queryParam("sinceAdded", formatDateSince(addedSince));
                        }

                        return requestBuilder.build();
                    })
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .retrieve()
                    .body(BrandwatchMentionResponse.class);

            if (mentions == null) {
                throw new UserException("brandwatch mention API call failed empty response",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            logger.info("got ({}) mentions from query ({}) successfully",
                    mentions.getResults().size(), queryID);

            return mentions;

        } catch (RestClientException ex) {
            throw new UserException(String.format("brandwatch mention API call failed: %s", ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private RestClient getRestClient() {
        if (this.restClient == null) {
            this.restClient = RestClient.builder()
                    .baseUrl(host)
                    .build();
        }
        return this.restClient;
    }

    private void login() {
        if (this.accessTokenExpiresAt == null || this.accessTokenExpiresAt.isBefore(LocalDateTime.now())) {
            logger.info("logging into brandwatch...");
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("password", this.password);

            try {
                BrandwatchLoginResponse loginResponse = this.getRestClient().post()
                        .uri(uriBuilder ->
                                uriBuilder.path("/oauth/token")
                                        .queryParam("username", this.username)
                                        .queryParam("grant_type", "api-password")
                                        .queryParam("client_id", "brandwatch-api-client")
                                        .build())
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .body(body)
                        .retrieve()
                        .body(BrandwatchLoginResponse.class);

                if (loginResponse == null) {
                    throw new UserException("brandwatch login failed empty response",
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }

                this.accessToken = loginResponse.getAccessToken();
                this.accessTokenExpiresAt = LocalDateTime.now().plusSeconds(loginResponse.getSecondsToExpire());

            } catch (RestClientException ex) {
                throw new UserException(String.format("brandwatch login failed: %s",
                        ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        logger.info("logged in successfully or already logged in");
    }

    private String formatDateSince(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'+0000'");
        String formattedDate = date.format(formatter);
        return URLEncoder.encode(formattedDate, StandardCharsets.UTF_8);
    }
}
