package com.kgm.restful_web_services.versioning;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class VersioningPersonSBIController {
//
//    //URI Versioning
//
//    @GetMapping("/{version}/personsbi")
//    public PersonV1 getFirstVersionOfPerson(){
//        return new PersonV1("Kazi Mahtab");
//    }
//
//    @GetMapping("/v2/personsbi")
//    public PersonV2 getSecondVersionOfPerson(){
//        return new PersonV2(new Name("Kazi","Mahtab"));
//    }
//
//    //Request Parameter Versioning
//
//    @GetMapping(path="/personsbi",params = "version=1")
//    public PersonV1 getFirstVersionOfPersonRequestParameter(){
//        return new PersonV1("Kazi Mahtab");
//    }
//
//    @GetMapping(path="/personsbi",params = "version=2")
//    public PersonV2 getSecondVersionOfPersonRequestParameter(){
//        return new PersonV2(new Name("Kazi","Mahtab"));
//    }
//
//    // (Custom) Headers versioning
//    @GetMapping(path="/personsbi/header",headers = "X-API-VERSION=1")
//    public PersonV1 getFirstVersionOfPersonRequestHeader(){
//        return new PersonV1("Kazi Mahtab");
//    }
//
//    @GetMapping(path="/personsbi/header",headers = "X-API-VERSION=2")
//    public PersonV2 getSecondVersionOfPersonRequestHeader(){
//        return new PersonV2(new Name("Kazi","Mahtab"));
//    }
//
//    //Media type versioning
//
//    @GetMapping(path="/personsbi/accept", produces = "application/vnd.company.app-v1+json")
//    public PersonV1 getFirstVersionOfPersonAccessHeader(){
//        return new PersonV1("Kazi Mahtab");
//    }
//
//    @GetMapping(path="/personsbi/accept", produces = "application/vnd.company.app-v2+json")
//    public PersonV2 getSecondVersionOfPersonAccessHeader(){
//        return new PersonV2(new Name("Kazi","Mahtab"));
//   }


}
