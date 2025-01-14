import { useRef, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import styled from 'styled-components';
import { ArrowForwardIos, ArrowForward } from '@material-ui/icons';
import TextareaAutosize from 'react-textarea-autosize';
import Columns from './Columns';
import { dummyDB as data } from '../App';

function Detail({ card }) {
  const history = useHistory();
  const [isShow, setIsShow] = useState(false);

  const inputRef = useRef();

  const toggleShow = () => setIsShow(prev => !prev);

  const handlePage = () => history.push('/');

  const handleSubmit = e => {
    e.preventDefault();

    console.log(inputRef.current.value);
    inputRef.current.value = '';
    inputRef.current.style.height = '39px';
  };

  return (
    <DetailBase>
      {!card ? (
        <span>Loading...</span>
      ) : (
        <DetailBlock>
          <ImgBlock>
            <img src={card.storeFileName} alt={`${card.title} 사진`} />
            <Buttons>
              <Button>
                <img src="/images/다운로드.png" alt="다운로드" />
              </Button>
              <Button>
                <img src="/images/하트.png" alt="좋아요" />
              </Button>
            </Buttons>
          </ImgBlock>
          <Info>
            <Wrapper>
              <UserInfo>{card.title}</UserInfo>
              <InfoButton>
                <img src="/images/라이크.png" alt="좋아요 버튼" />
              </InfoButton>
            </Wrapper>
            <Wrapper>
              <UserInfo>{card.authorNickName}</UserInfo>
              <InfoButton>
                <img src="/images/스크랩.png" alt="스크랩 버튼" />
              </InfoButton>
            </Wrapper>
            <UserInfo as="p">{card.content}</UserInfo>
            <Wrapper>
              <SmallUserInfo>
                <span>업로드날짜</span>
                <span>{card.createDate}</span>
              </SmallUserInfo>
              <SmallUserInfo>
                <span>카드태그</span>
                <span>태그1</span>
              </SmallUserInfo>
            </Wrapper>
            <CommentWrapper>
              <h3>댓글</h3>
              <span>몇 개</span>
              <CommentButton onClick={toggleShow} isShow={isShow}>
                <ArrowForwardIos />
              </CommentButton>
            </CommentWrapper>
            {!isShow && (
              <>
                <CommentList>
                  <CommentItem>
                    <Link to="">
                      <img
                        src="/images/업로더-사진.png"
                        alt={`아이디 이미지`}
                      />
                    </Link>
                    <CommentInfo>
                      <UserLink to="/member/:id">아이디</UserLink>
                      <CommentContent>내용내용</CommentContent>
                    </CommentInfo>
                  </CommentItem>
                </CommentList>
                <CommentForm onSubmit={handleSubmit}>
                  <CommentFormItem as="div">
                    <Link to="">
                      <img
                        src="/images/업로더-사진.png"
                        alt={`아이디 이미지`}
                      />
                    </Link>
                    <CommentText
                      type="text"
                      ref={inputRef}
                      placeholder="댓글을 입력하세요."
                    />
                    <button type="submit">완료</button>
                  </CommentFormItem>
                </CommentForm>
              </>
            )}
          </Info>
        </DetailBlock>
      )}
      <BackButton onClick={handlePage}>
        <ArrowForward />
      </BackButton>
      <Line />
      <Announcement>비슷한 순간들을 확인하세요</Announcement>
      <Columns data={data} />
    </DetailBase>
  );
}

export default Detail;

const DetailBase = styled.section`
  position: relative;
  padding: 50px 30px;
  text-align: center;

  > span {
    font-size: 20px;
  }
`;

const BackButton = styled.button`
  position: fixed;
  top: 100px;
  left: 14px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  transform: rotate(-180deg);

  :hover {
    background-color: #f0f0f0;
  }
`;

const DetailBlock = styled.div`
  display: flex;
  overflow: hidden;
  width: 1016px;
  min-height: 400px;
  margin: 30px auto 50px;
  border-radius: 32px;
  box-shadow: 5px 5px 8px rgba(0, 0, 0, 0.3);

  @media (max-width: 1015px) {
    flex-direction: column;
    width: 100%;
    max-width: 508px;
  }
`;

const ImgBlock = styled.div`
  overflow: hidden;
  position: relative;
  width: 50%;
  border-radius: 32px 0 0 32px;

  > img {
    display: block;
    width: 100%;
  }

  @media (max-width: 1015px) {
    width: 100%;
    border-radius: 0;
  }
`;

const Buttons = styled.div`
  display: flex;
  position: absolute;
  right: 20px;
  bottom: 20px;
`;

const Button = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #fff;

  & + & {
    margin-left: 6px;
  }

  > img {
    width: 100%;
  }
`;

const InfoButton = styled(Button)`
  overflow: hidden;
  width: 150px;
  margin-top: 8px;
  border-radius: 30px;
  background: none;
`;

const Info = styled.div`
  width: 50%;
  min-height: 100%;
  padding: 30px 30px 20px 30px;
  border-radius: 0 32px 32px 0;
  background-color: rgba(143, 143, 143, 0.15);

  @media (max-width: 1015px) {
    width: 100%;
    border-radius: 0;
  }
`;

const UserInfo = styled.div`
  display: flex;
  align-items: center;
  width: 100%;
  min-height: 50px;
  margin-top: 8px;
  padding: 10px;
  border-radius: 6px;
  background-color: #fff;
  text-align: left;
`;

const Wrapper = styled.div`
  display: flex;
  align-items: center;
  position: relative;
`;

const CommentButton = styled.button`
  display: flex;
  align-items: center;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  transform: ${({ isShow }) => (isShow ? 'rotate(0)' : 'rotate(90deg)')};
  transition: transform 0.1s ease-in-out;

  :hover {
    background-color: #f0f0f0;
  }

  svg {
    transform: translateX(3px);
    font-size: 14px;
  }
`;

const SmallUserInfo = styled(UserInfo)`
  width: 50%;
  height: 34px;
  min-height: 0;
  font-size: 14px;

  & + & {
    ::before {
      content: '';
      position: absolute;
      top: 20%;
      left: 50%;
      transform: translateX(-50%);
      width: 4px;
      height: 80%;
      background-color: #b580d1;
    }
  }

  > span:nth-of-type(1) {
    font-weight: 600;
    margin-right: 10px;
  }
`;

const CommentWrapper = styled.div`
  display: flex;
  align-items: center;
  margin-top: 10px;

  > * {
    margin-left: 10px;
    font-size: 18px;
    font-weight: 600;
  }
`;

const CommentList = styled.ul`
  margin-bottom: 50px;
`;

const CommentItem = styled.li`
  display: flex;

  > a {
    margin-top: 8px;

    img {
      width: 48px;
      height: 48px;
    }
  }
`;

const CommentInfo = styled(UserInfo)`
  flex-direction: column;
  align-items: flex-start;
  position: relative;
  width: 85%;
  margin-left: 10px;
`;

const UserLink = styled(Link)`
  margin-bottom: 8px;
  font-weight: 700;
  font-size: 14px;
`;

const CommentContent = styled.p`
  font-size: 12px;
  text-align: left;
`;

const Line = styled.div`
  width: 90%;
  margin: 0 auto;
  height: 4px;
  background-color: #b580d1;
`;

const Announcement = styled.p`
  margin: 16px 0;
  font-weight: 600;
  font-size: 22px;
`;

const CommentForm = styled.form``;

const CommentFormItem = styled(CommentItem)`
  width: 100%;

  > button {
    background-color: #b580d1;
    width: 50px;
    height: 40px;
    margin-top: 10px;
    margin-left: 10px;
    border-radius: 25px;
    color: #fff;
  }
`;

const CommentText = styled(TextareaAutosize)`
  resize: none;
  outline: none;
  border: none;
  flex: 1;
  margin-top: 10px;
  margin-left: 10px;
  padding: 10px;
  border-radius: 6px;
  line-height: 1.4;
`;
