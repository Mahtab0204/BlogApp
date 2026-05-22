package com.kgm.restful_web_services.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersioningPersonController {

    //URI Versioning

    @GetMapping("/v1/person")
    public PersonV1 getFirstVersionOfPerson(){
        return new PersonV1("Kazi Mahtab");
    }

    @GetMapping("/v2/person")
    public PersonV2 getSecondVersionOfPerson(){
        return new PersonV2(new Name("Kazi","Mahtab"));
    }

    //Request Parameter Versioning

    @GetMapping(path="/person",params = "version=1")
    public PersonV1 getFirstVersionOfPersonRequestParameter(){
        return new PersonV1("Kazi Mahtab");
    }

    @GetMapping(path="/person",params = "version=2")
    public PersonV2 getSecondVersionOfPersonRequestParameter(){
        return new PersonV2(new Name("Kazi","Mahtab"));
    }

    // (Custom) Headers versioning
    @GetMapping(path="/person/header",headers = "X-API-VERSION=1")
    public PersonV1 getFirstVersionOfPersonRequestHeader(){
        return new PersonV1("Kazi Mahtab");
    }

    @GetMapping(path="/person/header",headers = "X-API-VERSION=2")
    public PersonV2 getSecondVersionOfPersonRequestHeader(){
        return new PersonV2(new Name("Kazi","Mahtab"));
    }

    //Media type versioning

    @GetMapping(path="/person/accept", produces = "application/vnd.company.app-v1+json")
    public PersonV1 getFirstVersionOfPersonAccessHeader(){
        return new PersonV1("Kazi Mahtab");
    }

    @GetMapping(path="/person/accept", produces = "application/vnd.company.app-v2+json")
    public PersonV2 getSecondVersionOfPersonAccessHeader(){
        return new PersonV2(new Name("Kazi","Mahtab"));
    }


}
