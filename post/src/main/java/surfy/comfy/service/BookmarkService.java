package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.GetPostResponse;
import surfy.comfy.entity.read.Member;
import surfy.comfy.entity.write.Bookmark;
import surfy.comfy.entity.write.Post;
import surfy.comfy.repository.read.ReadBookmarkRepository;
import surfy.comfy.repository.read.ReadMemberRepository;
import surfy.comfy.repository.read.ReadPostRepository;
import surfy.comfy.repository.write.WriteBookmarkRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {
    private final WriteBookmarkRepository writeBookmarkRepository;
    private final ReadBookmarkRepository readBookmarkRepository;
    private final ReadPostRepository readPostRepository;
    private final ReadMemberRepository readMemberRepository;

    @Transactional
    public List<GetPostResponse> getBookmarks(Long memberId){
        
        List<Bookmark> bookmarkList=readBookmarkRepository.findAllByMemberId(memberId);
        List<GetPostResponse> bookmarks=new ArrayList<>();

        for(Bookmark b:bookmarkList){
            Post post=readPostRepository.findById(b.getPostId()).get();
            Member member=readMemberRepository.findById(post.getMemberId()).get();
            GetPostResponse postRes=new GetPostResponse(post,member,true,true);
            bookmarks.add(postRes);
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
        bookmark.setMemberId(memberId);
        bookmark.setPostId(postId);
        writeBookmarkRepository.save(bookmark);

        return "북마크 추가 성공";
    }

    @Transactional
    public String deleteBookmark(Long postId,Long memberId){
        Bookmark bookmark= writeBookmarkRepository.findByMemberIdAndPost_Id(memberId,postId);
        writeBookmarkRepository.delete(bookmark);

        return "북마크 삭제 성공";
    }
}
