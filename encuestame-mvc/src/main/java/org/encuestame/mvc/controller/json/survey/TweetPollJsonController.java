/*
 ************************************************************************************
 * Copyright (C) 2001-2010 encuestame: system online surveys Copyright (C) 2009
 * encuestame Development Team.
 * Licensed under the Apache Software License version 2.0
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to  in writing,  software  distributed
 * under the License is distributed  on  an  "AS IS"  BASIS,  WITHOUT  WARRANTIES  OR
 * CONDITIONS OF ANY KIND, either  express  or  implied.  See  the  License  for  the
 * specific language governing permissions and limitations under the License.
 ************************************************************************************
 */
package org.encuestame.mvc.controller.json.survey;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.encuestame.core.exception.EnMeExpcetion;
import org.encuestame.core.util.MD5Utils;
import org.encuestame.mvc.controller.AbstractJsonController;
import org.encuestame.persistence.domain.Question;
import org.encuestame.persistence.domain.security.SecUserSecondary;
import org.encuestame.utils.security.UnitTwitterAccountBean;
import org.encuestame.utils.web.UnitAnswersBean;
import org.encuestame.utils.web.UnitHashTag;
import org.encuestame.utils.web.UnitQuestionBean;
import org.encuestame.utils.web.UnitTweetPoll;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * TweetPoll Json Controller.
 *
 * @author Picado, Juan juanATencuestame.org
 * @since Dec 2, 2010 10:11:07 PM
 * @version $Id:$
 */
@Controller
public class TweetPollJsonController extends AbstractJsonController {

    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/api/survey/tweetpoll/publish.json", method = RequestMethod.GET)
    public ModelMap get(
            @RequestParam(value = "twitterAccounts", required = true) Long[] twitterAccountsId,
            @RequestParam(value = "question", required = true) String question,
            @RequestParam(value = "scheduled", required = true) Boolean scheduled,
            @RequestParam(value = "hashtags", required = true) String[] hashtags,
            @RequestParam(value = "answers", required = true) String[] answers,
            HttpServletRequest request, HttpServletResponse response)
            throws JsonGenerationException, JsonMappingException, IOException {
        final UnitTweetPoll tweetPoll = new UnitTweetPoll();
        // Get User Logged.
        final SecUserSecondary user = getByUsername(getUserPrincipalUsername());
        log.debug("user " +user.getUsername());
        if (user != null) {
            // set user id to question bean.
            final UnitQuestionBean questionBean = new UnitQuestionBean();
            questionBean.setQuestionName(question);
            questionBean.setUserId(user.getUid());
            log.debug("questionBean.name() " +questionBean.getQuestionName());
            //Add answers
            try {

                //Setting Answers.
                for (int row = 0; row < answers.length; row++) {
                    final UnitAnswersBean answer = new UnitAnswersBean();
                    answer.setAnswers(answers[row].trim());
                    answer.setAnswerHash(MD5Utils.md5(
                            String.valueOf(java.util.Calendar.getInstance().getTimeInMillis())
                            + RandomStringUtils.randomAlphabetic(2)));
                    questionBean.getListAnswers().add(answer);
                }

                //Setting Hash Tags
                for (int row = 0; row < hashtags.length; row++) {
                    final UnitHashTag hashTag = new UnitHashTag();
                    hashTag.setHashTagName(answers[row].toLowerCase().trim());
                    tweetPoll.getHashTags().add(hashTag);
                }

                log.debug("questionBean.getListAnswers() " +questionBean.getListAnswers().size());
                //save question
                  final Question questionDomain = getSurveyService().createQuestion(questionBean);
                // save create tweet poll
                    tweetPoll.setUserId(user.getSecUser().getUid());
                    tweetPoll.setCloseNotification(Boolean.FALSE);
                    tweetPoll.setResultNotification(Boolean.FALSE);
                    tweetPoll.setPublishPoll(Boolean.TRUE); //always TRUE
                    tweetPoll.setSchedule(scheduled);
                    getTweetPollService().createTweetPoll(tweetPoll, questionDomain);
                    // If publish is true and Scheduled is false, because if is
                    // scheduled we want
                    // send later.
                    if (tweetPoll.getPublishPoll()
                            && !tweetPoll.getSchedule()) {
                        String tweetText;
                            tweetText = getTweetPollService()
                                    .generateTweetPollText(tweetPoll, getUrlDomain(request, Boolean.TRUE));
                        final List<UnitTwitterAccountBean> accountBeans = new ArrayList<UnitTwitterAccountBean>();
                        for (int row = 0; row < twitterAccountsId.length; row++) {
                            final UnitTwitterAccountBean twitter = new UnitTwitterAccountBean();
                                   twitter.setAccountId(twitterAccountsId[row]);
                                   accountBeans.add(twitter);
                        }
                        log.debug("Accounts " + accountBeans.size());
                        getTweetPollService().publicMultiplesTweetAccounts(accountBeans,
                                              tweetPoll.getId(), tweetText);
                    }
                    log.debug("tweet poll created");
                setSuccesResponse();
            } catch (EnMeExpcetion e) {
                  log.error(e);
                  e.printStackTrace();
                  setError(e.getMessage(), response);
            } catch (NoSuchAlgorithmException e) {
                log.error(e);
                e.printStackTrace();
                setError(e.getMessage(), response);
            }
        } else {
            setError("access denied", response);
        }
        return returnData();
    }
}
