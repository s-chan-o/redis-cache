package redis.practice.member;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "Member", key = "#id")
    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "MemberList")
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public Member createMember(String name, String email) {
        Member member = Member.builder()
                .name(name)
                .email(email)
                .build();
        return memberRepository.save(member);
    }

    @Transactional
    @CacheEvict(value = "Member", key = "#id")
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));
        memberRepository.delete(member);
    }
}