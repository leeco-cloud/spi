package com.lee.spi.spi.example.springTest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author yanhuai lee
 */
@Controller
public class SpiController {

    @Resource
    private SpiTest spiTest;

    // http://127.0.0.1:8080/hello
    @RequestMapping("/hello")
    @ResponseBody
    public void hello(String name) {
        spiTest.test(name);
    }
}
