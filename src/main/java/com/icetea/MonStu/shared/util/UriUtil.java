package com.icetea.MonStu.shared.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public class UriUtil {

    // Custom URL 생성
    public static URI create(String pathSuffix, Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path(pathSuffix)
                .buildAndExpand(id)
                .toUri();
    }
}
