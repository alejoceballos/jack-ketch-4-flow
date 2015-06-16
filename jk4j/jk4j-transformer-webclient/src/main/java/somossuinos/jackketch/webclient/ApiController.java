package somossuinos.jackketch.webclient;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    @RequestMapping("/api")
    public String api() {
        return "Ok";
    }

}
