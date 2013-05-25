<%@ include file="/WEB-INF/jsp/includes/taglibs.jsp"%>
<article class="defaultMarginWrapper">
    <section class="web-tweetpoll-vote-options">

        <div class="web-permatlink yellow">
            <h2>
                <spring:message code="tweetpoll.votes.repeated" />
            </h2>
            <h3>
               ${tp_switch.tweetPoll.question.question}
            </h3>
            <h3>
                <spring:message code="tweetpoll.votes.selection" />: <b>${tp_switch.answers.answer}</b>
            </h3>
            <div class="web-tweetpoll-info">
                <a href="<%=request.getContextPath()%>/tweetpoll/${tp_switch.tweetPoll.tweetPollId}/${tp_switch.tweetPoll.question.slugQuestion}">
                    <spring:message code="tweetpoll.votes.link" />
                </a>
            </div>
        </div>

    </section>
</article>