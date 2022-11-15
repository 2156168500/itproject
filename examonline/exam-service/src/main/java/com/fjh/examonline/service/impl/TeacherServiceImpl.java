package com.fjh.examonline.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.fjh.examonline.dao.TeacherMapper;
import com.fjh.examonline.domain.Teacher;
import com.fjh.examonline.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public void save(Teacher teacher) {
        //生成助记码
        String mnemonicCode = PinyinUtil.getPinyin(teacher.getTname());
        teacher.setMnemonicCode(mnemonicCode);
        //密码加密
        String pass = DigestUtil.md5Hex(teacher.getPass());
        teacher.setPass(pass);
        teacherMapper.insert(teacher);
    }

    @Override
    public Teacher findTeacherByName(String tname) {
        return teacherMapper.findTeacherByName(tname);
    }
}
