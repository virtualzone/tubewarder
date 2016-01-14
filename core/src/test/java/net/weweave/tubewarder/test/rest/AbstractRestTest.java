package net.weweave.tubewarder.test.rest;

import com.jayway.restassured.http.ContentType;
import net.weweave.tubewarder.dao.UserDao;
import net.weweave.tubewarder.domain.User;
import net.weweave.tubewarder.service.model.ErrorCode;
import net.weweave.tubewarder.test.DbTestAssist;
import org.apache.http.HttpStatus;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.json.JSONObject;
import org.junit.Before;
import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;
import java.net.URL;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public abstract class AbstractRestTest {
    @ArquillianResource
    public URL deploymentUrl;

    @Inject
    private DbTestAssist dbTestAssist;

    @Inject
    private UserDao userDao;

    @Before
    public final void initialize() {
        getDbTestAssist().cleanDb();
    }

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "net.weweave.tubewarder")
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource("resources.xml")
                .addAsWebInfResource("openejb-jar.xml");
        return war;
    }

    public String getUri() {
        return deploymentUrl + "rs/" + getServiceName();
    }

    public abstract String getServiceName();

    protected User createAdminUser() {
        User user = new User();
        user.setUsername("admin");
        user.setDisplayName("System Administrator");
        user.setHashedPassword(BCrypt.hashpw("admin", BCrypt.gensalt()));
        user.setEnabled(true);
        user.setAllowAppTokens(true);
        user.setAllowChannels(true);
        user.setAllowTemplates(true);
        user.setAllowUsers(true);
        getUserDao().store(user);
        return user;
    }

    protected String authAdminGetToken() {
        JSONObject payload = new JSONObject();
        payload.put("username", "admin");
        payload.put("password", "admin");

        JSONObject result = new JSONObject(given()
                .body(payload.toString())
                .contentType(ContentType.JSON)
        .when()
                .post(deploymentUrl + "rs/auth")
                .asString());
        return result.getString("token");
    }

    public DbTestAssist getDbTestAssist() {
        return dbTestAssist;
    }

    public void setDbTestAssist(DbTestAssist dbTestAssist) {
        this.dbTestAssist = dbTestAssist;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
