package com.icetea.MonStu.service;

import com.icetea.MonStu.enums.*;
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

    public List<PostStatus> getPostStatus() { return List.of(PostStatus.values()); }
}
