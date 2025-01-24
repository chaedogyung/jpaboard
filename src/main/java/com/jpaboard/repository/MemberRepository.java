package com.jpaboard.repository;

import com.jpaboard.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository  extends JpaRepository<Member, Integer> {
    Member findByUseremail(String username);
    Member findByUserid(String userid);

    Member findByUsername(String username);
}
