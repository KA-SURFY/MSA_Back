package surfy.comfy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class InternalApiService<T> {
    private RestTemplate restTemplate;

    @Autowired
    public InternalApiService(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }
    public ResponseEntity<T> get(String url) {
        return callApiEndpoint(url, HttpMethod.GET, null, (Class<T>)Object.class);
    }
    public ResponseEntity<T> post(String url, Object body) {
        return callApiEndpoint(url, HttpMethod.POST, body, (Class<T>)Object.class);
    }
    private ResponseEntity<T> callApiEndpoint(String url, HttpMethod httpMethod, Object body, Class<T> clazz) {
        return restTemplate.exchange(url, httpMethod, new HttpEntity<>(body), clazz);
    }
}