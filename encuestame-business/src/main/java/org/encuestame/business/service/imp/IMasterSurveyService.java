 /************************************************************************************
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
package org.encuestame.business.service.imp;

import java.util.List;

import org.encuestame.core.service.ServiceOperations;
import org.encuestame.persistence.domain.question.Question;
import org.encuestame.persistence.domain.question.QuestionAnswer;
import org.encuestame.persistence.exception.EnMeExpcetion;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.utils.web.HashTagBean;
import org.encuestame.utils.web.QuestionAnswerBean;
import org.encuestame.utils.web.QuestionBean;

 /**
 * Master Survey Service Interface.
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since 27/05/2010 20:36:29
 * @version $Id:$
 */
public interface IMasterSurveyService extends ServiceOperations{

    /**
     * Suggestion Question List.
     * @param questionKeyword
     * @return
     */
    List<QuestionBean> listSuggestQuestion(final String questionKeyword, final String username) throws EnMeNoResultsFoundException;

    /**
     * List Suggested Hash Tags.
     * @param hashTagKeyWord
     * @param maxResults
     * @return
     */
    List<HashTagBean> listSuggestHashTags(final String hashTagKeyWord, final Integer maxResults);

    /**
     * Save Question Answer.
     * @param answerBean answer
     * @throws EnMeExpcetion EnMeExpcetion
     */
    QuestionAnswer createQuestionAnswer(
            final QuestionAnswerBean answerBean,
            final Question question);

    QuestionAnswer getQuestionAnswerById(final Long id) throws EnMeNoResultsFoundException;
}
