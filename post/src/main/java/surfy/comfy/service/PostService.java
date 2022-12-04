package surfy.comfy.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import surfy.comfy.data.manage.SurveyResponse;
import surfy.comfy.data.post.*;
import surfy.comfy.entity.read.Member;
import surfy.comfy.entity.write.Bookmark;
import surfy.comfy.entity.write.Post;
import surfy.comfy.entity.read.Satisfaction;
import surfy.comfy.entity.read.Survey;
import surfy.comfy.repository.read.*;
import surfy.comfy.repository.write.WriteBookmarkRepository;
import surfy.comfy.repository.write.WritePostRepository;
import surfy.comfy.type.SurveyType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final WritePostRepository writePostRepository;

    private final WriteBookmarkRepository writeBookmarkRepository;
    private final ReadPostRepository readPostRepository;
    private final ReadBookmarkRepository readBookmarkRepository;
    private final ReadSurveyRepository readSurveyRepository;
    private final ReadSatisfactionRepository readSatisfactionRepository;
    private final ReadMemberRepository readMemberRepository;
    private final Logger logger= LoggerFactory.getLogger(PostService.class);

    @Transactional
    public List<GetPostResponse> getMyposts(Long memberId){
        Member member=readMemberRepository.findById(memberId).get();
        // 내가 작성한 게시글들들
        List<Post> myPostList=readPostRepository.findAllByMemberId(memberId);
        List<GetPostResponse> myPosts=new ArrayList<>();
        for(Post p:myPostList){
            GetPostResponse my=new GetPostResponse(p,member,false,true);
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
        List<Post> allPostList=readPostRepository.findAll();
        List<GetPostResponse> allPosts=new ArrayList<>();

        Boolean member_case=false;
        Boolean isBookmarked=false;

        if(memberId==0) {
            member_case=false;
            for(Post p:allPostList){
                Member member=readMemberRepository.findById(p.getMemberId()).get();
                GetPostResponse post=new GetPostResponse(p,member,isBookmarked,member_case);
                allPosts.add(post);
            }
            return allPosts;
        }
        else member_case=true;

        for(Post p: allPostList){
            Bookmark bookmark=readBookmarkRepository.findByMemberIdAndPostId(memberId,p.getId());
//            Member member=readMemberRepository.findById(p.getMemberId()).get();
            Member member=readMemberRepository.findById(memberId).get();

            if(bookmark==null){
                isBookmarked=false;
            }
            else{
                isBookmarked=true;
            }
            GetPostResponse post=new GetPostResponse(p,member,isBookmarked,member_case);
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
//    @Cacheable(value = "post", key = "#postId", cacheManager = "CacheManager")
    @Transactional
    public GetPostResponse getPost(Long postId,Long memberId){
        logger.info("[getPost] - memberId: {}",memberId);

        Post post=readPostRepository.findById(postId).get();
        Survey survey= readSurveyRepository.findSurveysById(post.getSurveyId());
        List<Satisfaction> allSatisfactions=readSatisfactionRepository.findAllBySurveyId(survey.getId());
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


        if(memberId!=0){ // 비회원
            Long member_id=memberId;
            member_case=true;
            Bookmark bookmark=readBookmarkRepository.findByMemberIdAndPostId(member_id,postId);
            if(bookmark==null){
                isBookmarked=false;
            }
            else{
                isBookmarked=true;
            }
        }

        GetPostResponse response=new GetPostResponse(post,isBookmarked,member_case,survey.getSatisfaction(),average);

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
    public DeletePostResponse deletePost(Long postId, Long memberId){
        Post post=writePostRepository.findById(postId).get();
        // 해당 게시글을 북마크한 사람들의 북마크 삭제
        List<Bookmark> bookmarks=writeBookmarkRepository.findAllByPostId(postId);
        for(Bookmark bookmark:bookmarks){
            writeBookmarkRepository.delete(bookmark);
        }
        writePostRepository.delete(post);

        return new DeletePostResponse(postId,memberId);
    }

    /**
     * 게시글 검색
     * @param title
     * @return
     */
    @Transactional
    public List<PostResponse> searchPost(String title){
        List<Post> SearchList = readPostRepository.findByTitleContaining(title);
        List<PostResponse> search=SearchList.stream()
                .map(p -> new PostResponse(p))
                .collect(Collectors.toList());

        System.out.println("service:"+search);
        return search;
    }
    /**
     * 설문 완료된 내 설문지 조회
     * @param memberId
     * @return
     */
    @Transactional
    public List<SurveyResponse> getMySurvey(Long memberId){
        List<Survey> mySurveyList = readSurveyRepository.findAllByMemberIdAndStatus(memberId, SurveyType.finish);
        List<SurveyResponse> response=new ArrayList<>();

        int average=0;

        for(Survey s:mySurveyList){
            List<Satisfaction> allSatisfactions=readSatisfactionRepository.findAllBySurveyId(s.getId());
            Long total=0L;

            if(allSatisfactions.size()==0){
                average=0;
            }
            else{
                for(Satisfaction sf:allSatisfactions){
                    total+=sf.getPercent();
                }
                average= total.intValue()/allSatisfactions.size();
                System.out.println("satisfaction average: "+average);
            }
            response.add(new SurveyResponse(s,average));
        }

        return response;
    }

    /**
     * 게시글 등록
     * @param request
     * @return
     */
    @Transactional
    public RequestPost registerPost(RequestPost request){
        logger.info("[PostService - registerPost] : request.title: {}",request.getTitle());
        Survey survey=readSurveyRepository.findById(request.getSurveyId()).get();
        Post post=new Post();
        post.setTitle(request.getTitle());
        post.setContents(request.getContents());
        post.setMemberId(request.getMemberId());
        post.setSurveyId(request.getSurveyId());
        post.setUploadDate(LocalDateTime.now());
        post.setThumbnail(survey.getThumbnail());
        return new RequestPost(writePostRepository.saveAndFlush(post));
    }
}
