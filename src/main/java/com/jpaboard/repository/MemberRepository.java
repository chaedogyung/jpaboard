package com.jpaboard.repository;

import com.jpaboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository  extends JpaRepository<Member, Integer> {
    Member findByUseremail(String useremail);
    Member findByUserid(String userid);
}
