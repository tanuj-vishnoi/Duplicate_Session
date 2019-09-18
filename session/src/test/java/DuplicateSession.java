import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.remote.http.W3CHttpCommandCodec;
import org.openqa.selenium.remote.http.W3CHttpResponseCodec;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;


class Duplicate {
    public static RemoteWebDriver createDriverFromSession(final SessionId sessionId, URL server_addr)
    {
        CommandExecutor executor = new HttpCommandExecutor(server_addr)
        {

            @Override
            public Response execute(Command command) throws IOException {
                Response response = null;

                if (command.getName() == "newSession")
                {
                    response = new Response();
                    response.setSessionId(sessionId.toString());
                    response.setStatus(0);
                    response.setValue(Collections.<String, String>emptyMap());

                    try {
                        Field commandCodec = null;
                        commandCodec = this.getClass().getSuperclass().getDeclaredField("commandCodec");
                        commandCodec.setAccessible(true);
                        commandCodec.set(this, new W3CHttpCommandCodec());

                        Field responseCodec = null;
                        responseCodec = this.getClass().getSuperclass().getDeclaredField("responseCodec");
                        responseCodec.setAccessible(true);
                        responseCodec.set(this, new W3CHttpResponseCodec());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                } else {
                    response = super.execute(command);
                }
                return response;
            }
        };

        return new RemoteWebDriver(executor, new DesiredCapabilities());
    }
}


public class DuplicateSession
{
    public static void main(String [] args) throws MalformedURLException {

        SessionId session_id = new SessionId("38ade9e053d683ce7ad616e9b40277e0");
        URL url = new URL("http://localhost:32814");

        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\res2\\chromedriver.exe");

//        RemoteWebDriver driver1 = new ChromeDriver();
//        HttpCommandExecutor executor = (HttpCommandExecutor) driver1.getCommandExecutor();
//
//        URL url = executor.getAddressOfRemoteServer();
//        SessionId session_id = driver1.getSessionId();
//
//        driver1.navigate().to("http://www.google.com");


        RemoteWebDriver driver2 = Duplicate.createDriverFromSession(session_id, url);
        driver2.get("http://www.yahoo.in");
        System.out.println(driver2.getSessionId());
    }
}
