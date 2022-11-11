package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.post.*;
import surfy.comfy.entity.*;
import surfy.comfy.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    private final SatisfactionRepository satisfactionRepository;
    private final Logger logger= LoggerFactory.getLogger(PostService.class);

    @Transactional
    public List<GetPostResponse> getMyposts(Long memberId){

        // 내가 작성한 게시글들들
        List<Post> myPostList=postRepository.findAllByMember_Id(memberId);
        List<GetPostResponse> myPosts=new ArrayList<>();
        for(Post p:myPostList){
            GetPostResponse my=new GetPostResponse(p,false,true);
            myPosts.add(my);
        }


        return myPosts;
    }

    /**
     * 모든 게시글 조회
     * @return allPosts
     */
    @Transactional
    public List<GetPostResponse> getAllPosts(Long memberId){
        List<Post> allPostList=postRepository.findAll();
        List<GetPostResponse> allPosts=new ArrayList<>();

        Boolean member_case=false;
        Boolean isBookmarked=false;

        if(memberId==0) {
            member_case=false;
            for(Post p:allPostList){
                GetPostResponse post=new GetPostResponse(p,isBookmarked,member_case);
                allPosts.add(post);
            }
            return allPosts;
        }
        else member_case=true;

        for(Post p: allPostList){
            Bookmark bookmark=bookmarkRepository.findByMember_IdAndPost_Id(memberId,p.getId());

            if(bookmark==null){
                isBookmarked=false;
            }
            else{
                isBookmarked=true;
            }
            GetPostResponse post=new GetPostResponse(p,isBookmarked,member_case);
            allPosts.add(post);
        }
        return allPosts;
    }

    /**
     * minseo
     * 게시글 info 조회
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional
    public GetPostResponse getPost(Long postId,Long memberId){
        logger.info("[getPost] - memberId: {}",memberId);

        Post post=postRepository.findById(postId).get();
        List<Satisfaction> allSatisfactions=satisfactionRepository.findAllBySurvey_Id(post.getSurvey().getId());
        Long total=0L;
        int average;

        if(allSatisfactions.size()==0){
            average=0;
        }
        else{
            for(Satisfaction s:allSatisfactions){
                total+=s.getPercent();
            }
            average= total.intValue()/allSatisfactions.size();
            System.out.println("satisfaction average: "+average);
        }

        Boolean isBookmarked=false;
        Boolean member_case=false;


        if(memberId==(long)0){ // 비회원
            member_case=false;
        }
        else{ // 회원
            Long member_id=memberId;
            member_case=true;
            Bookmark bookmark=bookmarkRepository.findByMember_IdAndPost_Id(member_id,postId);
            if(bookmark==null){
                isBookmarked=false;
            }
            else{
                isBookmarked=true;
            }
        }

        GetPostResponse response=new GetPostResponse(post,isBookmarked,member_case,post.getSurvey().getSatisfaction(),average);

        return response;
    }

    /**
     * minseo
     * 게시글 삭제
     * @param postId
     * @param memberId
     * @return
     */
    @Transactional
    public DeletePostResponse deletePost(Long postId, String memberId){
        Post post=postRepository.findById(postId).get();

        // 해당 게시글을 북마크한 사람들의 북마크 삭제
        List<Bookmark> bookmarks=bookmarkRepository.findAllByPost_Id(postId);
        for(Bookmark bookmark:bookmarks){
            bookmarkRepository.delete(bookmark);
        }

        postRepository.delete(post);

        return new DeletePostResponse(postId,Long.parseLong(memberId));
    }

    /**
     * 게시글 검색
     * @param title
     * @return
     */
    @Transactional
    public List<PostResponse> searchPost(String title){
        List<Post> SearchList = postRepository.findByTitleContaining(title);
        List<PostResponse> search=SearchList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());

        System.out.println("service:"+search);
        return search;
    }

}
