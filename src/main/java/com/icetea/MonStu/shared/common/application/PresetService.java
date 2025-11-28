package com.icetea.MonStu.shared.common.application;

import com.icetea.MonStu.member.enums.CountryCode;
import com.icetea.MonStu.member.enums.MemberRole;
import com.icetea.MonStu.member.enums.MemberStatus;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
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
