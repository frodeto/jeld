package no.torvundconsulting;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Jeld {

    private ElasticsearchClient esClient;

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Provide elastic http-host, username and password as the three first program arguments");
            System.exit(-1);
        }
        try {
            String path = new File(".").getCanonicalPath();
            System.out.println("Working directory: " + path);
        } catch (IOException e) {
            System.out.println("Could not find pwd");
            System.exit(-1);
        }
        new Jeld().test(args[0], args[1], args[2]);
    }

    void test(String httpHost, String user, String pw) {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pw));

        RestClientBuilder builder = RestClient.builder(
                        new HttpHost(httpHost, 9200))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider));

        try (RestClient restClient = builder.build()) {

            ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
            esClient = new ElasticsearchClient(transport);

            boolean created = createIndexIfNeeded();
            if (! created) {
                System.out.println("Could not create index");
                System.exit(- 1);
            }
            populateIndex();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(- 1);
        }
    }

    private void populateIndex() throws IOException {
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
        try(FileReader input = new FileReader(new File(new File(".").getCanonicalPath(), "ad-index.json"));) {
            CreateIndexRequest req = CreateIndexRequest.of(b -> b
                    .index("ad-index")
                    .withJson(input)
            );

            created = this.esClient.indices().create(req).acknowledged();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return created;
    }

    private void createJsonDataFile() {
        Gson gson = new Gson();
        List<Ad> ads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ads.add(Ad.buildRandomAd());
        }
        try (FileWriter fw = new FileWriter("jeld.json", true);
                BufferedWriter bufferedWriter = new BufferedWriter(fw)) {
                bufferedWriter.write(gson.toJson(ads));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private record Ad(
            String key,
            String type,
            String h1,
            String v1) {

        static Ad buildRandomAd() {
            Random rand = new Random();
            return new Ad(
                    keys.get(rand.nextInt(keys.size())),
                    types.get(rand.nextInt(keys.size())),
                    h1s.get(rand.nextInt(keys.size())),
                    v1s.get(rand.nextInt(keys.size())));
        }
    }

    static List<String> keys = List.of("Til Salgs", "Solgt", "Forkjøp");
    static List<String> types = List.of("Enebolig", "Rekkehus", "Leilighet");
    static List<String> h1s = List.of("Boareal", "Brutto", "netto");
    static List<String> v1s = List.of("Kjøkken", "Stue", "Soverom");

}
