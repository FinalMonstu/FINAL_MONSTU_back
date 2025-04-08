package com.icetea.MonStu.service;

import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.LanguageCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PresetService {

    public List<String> getLanguageList() {
        return LanguageCode.getLanguageList();
    }

    public List<String> getCountryList() { return CountryCode.getCountryNames(); }
}
