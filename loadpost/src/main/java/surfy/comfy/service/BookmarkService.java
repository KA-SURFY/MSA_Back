package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.GetPostResponse;
import surfy.comfy.entity.Bookmark;
import surfy.comfy.repository.BookmarkRepository;
import surfy.comfy.repository.MemberRepository;
import surfy.comfy.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {
    @Autowired
    private final BookmarkRepository bookmarkRepository;
    @Autowired
    private final MemberRepository memberRepository;
    @Autowired
    private final PostRepository postRepository;

    @Cacheable(value = "bookmark", key = "#memberId", cacheManager = "CacheManager")
    @Transactional
    public List<GetPostResponse> getBookmarks(Long memberId){
        List<Bookmark> bookmarkList=bookmarkRepository.findAllByMember_Id(memberId);
        List<GetPostResponse> bookmarks=new ArrayList<>();

        for(Bookmark b:bookmarkList){
            GetPostResponse post=new GetPostResponse(b.getPost(),true,true);
            bookmarks.add(post);
        }
        return bookmarks;
    }

    /**
     * 북마크 추가
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional
    public String addBookmark(Long postId, Long memberId){
        Bookmark bookmark=new Bookmark();
        bookmark.setMember(memberRepository.findById(memberId).get());
        bookmark.setPost(postRepository.findById(postId).get());

        bookmarkRepository.save(bookmark);

        return "북마크 추가 성공";
    }

    @Transactional
    public String deleteBookmark(Long postId,Long memberId){
        Bookmark bookmark= bookmarkRepository.findByMember_IdAndPost_Id(memberId,postId);
        bookmarkRepository.delete(bookmark);

        return "북마크 삭제 성공";
    }
}
