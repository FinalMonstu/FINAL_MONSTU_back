package com.icetea.MonStu.service;

import com.icetea.MonStu.enums.CountryCode;
import com.icetea.MonStu.enums.LanguageCode;
import com.icetea.MonStu.enums.MemberRole;
import com.icetea.MonStu.enums.MemberStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PresetService {

    public List<String> getLanguageList() {  return LanguageCode.getLanguageList(); }

    public List<String> getCountryList() { return CountryCode.getCountryNames(); }

    public List<MemberStatus> getMemberStatus() { return List.of(MemberStatus.values()); }

    public List<MemberRole> getMemberRole() { return List.of(MemberRole.values()); }
}
