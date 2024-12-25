/*package com.jpaboard;

import com.jpaboard.dto.MemberFormDto;
import com.jpaboard.entity.Member;
import com.jpaboard.repository.BoardRepository;
import com.jpaboard.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application.properties")
class JpaboardApplicationTests {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager em;*/

/*    @Test
    @DisplayName("게시글 저장 테스트")
    public void boardInsert() {
        BoardVO boardVO = new BoardVO();
        boardVO.setTitle("테스트 제목");
        boardVO.setContent("테스트 내용");
        boardVO.setWriter("테스트 작성자");

        BoardVO bovo = boardRepository.save(boardVO);
        System.out.println(bovo);
    }*/

/*    @Test
    @DisplayName("게시글 조회 테스트")
    public void boardSelect() {
        List<BoardVO> list = boardRepository.findByTitle("test");
        for (BoardVO boardVO : list) {
            System.out.println(boardVO.toString());
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트2")
    public void boardSelect2() {
        List<BoardVO> list = boardRepository.findByTitleOrContent("test", "테스트 상세 설명");
        for (BoardVO boardVO : list) {
            System.out.println(boardVO.toString());
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트3")
    public void boardSelect3() {
        List<BoardVO> list = boardRepository.findByBnoLessThanOrderByBnoAsc(58);
        for (BoardVO boardVO : list) {
            System.out.println(boardVO.toString());
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트4")
    public void boardSelect4() {
        List<BoardVO> list = boardRepository.findByContent("테스트 상세 설명");
        for (BoardVO boardVO : list) {
            System.out.println(boardVO.toString());
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트5")
    public void boardSelect5() {
        List<BoardVO> list = boardRepository.findByContentByNative("테스트 상세 설명");
        for (BoardVO boardVO : list) {
            System.out.println(boardVO.toString());
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트6")
    public void boardSelect6() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QBoardVO qBoardVO = QBoardVO.boardVO;
        JPAQuery<BoardVO> query = queryFactory.selectFrom(qBoardVO)
                .where(qBoardVO.title.eq("test"))
                .where(qBoardVO.content.like("%" + "test" + "%"))
                .orderBy(qBoardVO.bno.desc());
        List<BoardVO> list = query.fetch();
        for (BoardVO boardVO : list) {
            System.out.println(boardVO.toString());
        }
    }

    @Test
    @DisplayName("게시글 조회 테스트7")
    public void boardSelect7() {
        BooleanBuilder builder = new BooleanBuilder();
        QBoardVO qBoardVO = QBoardVO.boardVO;
        String content = "테스트 상세 설명";
        int bno = 1;
        String title = "jpatest";

        builder.and(qBoardVO.content.like("%" + content + "%"));
        builder.and(qBoardVO.bno.gt(bno));
        builder.and(qBoardVO.title.eq(title));

        Pageable pageable = PageRequest.of(0, 5);
        Page<BoardVO> boardPagingResult = boardRepository.findAll(builder, pageable);

        System.out.println("총 갯수 :" + boardPagingResult.getTotalElements());

        List<BoardVO> boardList = boardPagingResult.getContent();
        for (BoardVO resulboardVO : boardList) {
            System.out.println(resulboardVO.toString());
        }

    }*/

 /*   public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setUserid("user");
        memberFormDto.setUseremail("test@gmail.com");
        memberFormDto.setUsername("홍길동");
        memberFormDto.setUserpass("1234");
        memberFormDto.setUseraddress("서울시 마포구 합정동");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getUseremail(), saveMember.getUseremail());
        assertEquals(member.getUserid(), saveMember.getUserid());
        assertEquals(member.getUsername(), saveMember.getUsername());
        assertEquals(member.getUseraddress(), saveMember.getUseraddress());
        assertEquals(member.getUserpass(), saveMember.getUserpass());
    }
*/
/*}*/
