package es.upm.miw.restControllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import es.upm.miw.dataServices.DatabaseSeederService;
import es.upm.miw.dtos.TokenOutputDto;
import es.upm.miw.restControllers.TokenResource;

@Service
public class RestService {

    @Autowired
    private Environment environment;

    @Autowired
    private DatabaseSeederService databaseSeederService;

    @Value("${server.contextPath}")
    private String contextPath;

    @Value("${miw.admin.mobile}")
    private String adminMobile;

    @Value("${miw.admin.password}")
    private String adminPassword;
    private TokenOutputDto tokenDto;

    @PostConstruct
    public void constructor() {
        this.reLoadTestDB();
    }

    private int port() {
        return Integer.parseInt(environment.getProperty("local.server.port"));
    }

    public <T> RestBuilder<T> restBuilder(RestBuilder<T> restBuilder) {
        restBuilder.port(this.port());
        restBuilder.path(contextPath);
        if (tokenDto != null) {
            restBuilder.basicAuth(tokenDto.getToken());
        }
        return restBuilder;
    }

    public RestBuilder<Object> restBuilder() {
        RestBuilder<Object> restBuilder = new RestBuilder<>(this.port());
        restBuilder.path(contextPath);
        if (tokenDto != null) {
            restBuilder.basicAuth(tokenDto.getToken());
        }
        return restBuilder;
    }

    public RestService loginAdmin() {
        this.tokenDto = new RestBuilder<TokenOutputDto>(this.port()).path(contextPath).path(TokenResource.TOKENS)
                .basicAuth(this.adminMobile, this.adminPassword).clazz(TokenOutputDto.class).post().build();
        return this;
    }

    public RestService loginManager() {
        this.tokenDto = new RestBuilder<TokenOutputDto>(this.port()).path(contextPath).path(TokenResource.TOKENS).basicAuth("666666001", "p001")
                .clazz(TokenOutputDto.class).post().build();
        return this;
    }

    public RestService loginOperator() {
        this.tokenDto = new RestBuilder<TokenOutputDto>(this.port()).path(contextPath).path(TokenResource.TOKENS).basicAuth("666666005", "p005")
                .clazz(TokenOutputDto.class).post().build();
        return this;
    }

    public RestService loginCustomer() {
        this.tokenDto = new RestBuilder<TokenOutputDto>(this.port()).path(contextPath).path(TokenResource.TOKENS).basicAuth("666666002", "p002")
                .clazz(TokenOutputDto.class).post().build();
        return this;
    }

    public RestService logout() {
        this.tokenDto = null;
        return this;
    }

    public void reLoadTestDB() {
        this.databaseSeederService.resetDB();
    }

    public TokenOutputDto getTokenDto() {
        return tokenDto;
    }

    public void setTokenDto(TokenOutputDto tokenDto) {
        this.tokenDto = tokenDto;
    }

    public String getAdminMobile() {
        return adminMobile;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

}
