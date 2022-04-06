package no.torvundconsulting;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jeld {

    private ElasticsearchClient esClient;

    public static void main(String[] args) {
        if (args.length < 4 || (!args[0].equals("index") && !args[0].equals("query"))) {
            System.out.println("Provide mode <index|query> as first argument");
            System.out.println("Provide elastic http-host, username and password as the three next program arguments");
            System.exit(-1);
        }
        Jeld jeld = new Jeld();

        RestClient restClient = jeld.initialize(args);

        if (args[0].equals("index")) {
            jeld.testIndex();
        }
        jeld.testQuery();

        try {
            restClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testQuery() {
        String dateTime = LocalDateTime.now().minusDays(5).format(DateTimeFormatter.ISO_DATE_TIME);

        Query rangeQuery = new Query.Builder()
                .range(r -> r.field("dato").lte(JsonData.of(dateTime))).build();

        Query matchQuery = new Query.Builder()
                .match(m -> m.field("type").query("Leilighet")).build();

        try {
            SearchResponse<Ad> searchResponse = esClient.search(SearchRequest.of(
                    builder -> builder.query(matchQuery).query(rangeQuery).index("ad-index")), Ad.class);
            HitsMetadata<Ad> hitsMetadata = searchResponse.hits();
            List<Hit<Ad>> hits = hitsMetadata.hits();
            System.out.println("Number of hits: " + (hitsMetadata.total() != null ? hitsMetadata.total().value() : 0));
            hits.forEach(adHit -> System.out.println(adHit.source()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Query failed");
            System.exit(-1);
        }
    }

    private RestClient initialize(String[] args) {
        RestClient restClient = buildEsClient(args[1], args[2], args[3]);
        boolean created = createIndexIfNeeded();
        if (! created) {
            System.out.println("Could not create index");
            System.exit(- 1);
        }
        return restClient;
    }

    void testIndex() {
        populateIndex();
    }

    private RestClient buildEsClient(String httpHost, String user, String pw) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pw));

        RestClientBuilder builder = RestClient.builder(
                        new HttpHost(httpHost, 9200))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        RestClient restClient = builder.build();
        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
        esClient = new ElasticsearchClient(transport);
        return restClient;
    }

    private void populateIndex() {
        List<Ad> ads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ads.add(Ad.buildRandomAd());
        }

        Gson gson = new Gson();
        ads.forEach(ad -> {
            System.out.println(" *** " + gson.toJson(ad));
            try {
                esClient.index(IndexRequest.of(b -> b
                        .index("ad-index")
                        .document(ad)));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to index");
            }
        });
    }

    boolean createIndexIfNeeded() {
        String index = CreateIndexRequest.of(builder -> builder.index("ad-index")).index();
        if (index != null) return true;
        boolean created = false;
        try(FileReader input = new FileReader(new File(new File(".").getCanonicalPath(), "ad-index.json"))) {
            CreateIndexRequest req = CreateIndexRequest.of(b -> b
                    .index("ad-index")
                    .withJson(input)
            );

            created = Boolean.TRUE.equals(this.esClient.indices().create(req).acknowledged());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return created;
    }

    private record Ad(
            String key,
            String type,
            String h1,
            String v1,
            String dato) {

        static Ad buildRandomAd() {
            Random rand = new Random();
            return new Ad(
                    keys.get(rand.nextInt(keys.size())),
                    types.get(rand.nextInt(keys.size())),
                    h1s.get(rand.nextInt(keys.size())),
                    v1s.get(rand.nextInt(keys.size())),
                    LocalDateTime.now()
                            .minusDays(rand.nextInt(10))
                            .plusHours(rand.nextInt(10))
                            .format(DateTimeFormatter.ISO_DATE_TIME));
        }
    }

    static List<String> keys = List.of("Til Salgs", "Solgt", "Forkjøp");
    static List<String> types = List.of("Enebolig", "Rekkehus", "Leilighet");
    static List<String> h1s = List.of("Boareal", "Bruttoareal", "Nettoareal");
    static List<String> v1s = List.of("Kjøkken", "Stue", "Soverom");

}
