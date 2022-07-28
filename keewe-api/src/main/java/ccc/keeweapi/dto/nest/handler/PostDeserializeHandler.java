package ccc.keeweapi.dto.nest.handler;

import ccc.keeweapi.dto.nest.AbstractPostCreateRequest;
import ccc.keeweapi.dto.nest.CommonPostCreateRequest;
import ccc.keeweapi.dto.nest.VotePostCreateRequest;
import ccc.keewecore.consts.KeeweConsts;
import ccc.keewecore.consts.KeeweRtnConsts;
import ccc.keewecore.exception.KeeweException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class PostDeserializeHandler extends JsonDeserializer<AbstractPostCreateRequest> {
    private final Validator validator;
    private final ObjectMapper objectMapper;

    @Override
    public AbstractPostCreateRequest deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectNode requestTree = objectMapper.readTree(p);
        log.info("[deserialize] :: requestTree {}", requestTree);

        Assert.notNull(requestTree, KeeweRtnConsts.ERR400.getDescription());
        Assert.notNull(requestTree.get(KeeweConsts.POST_TYPE_FIELD), KeeweRtnConsts.ERR506.getDescription());

        Class<?> requestClazz = postRequestFactory(requestTree.get(KeeweConsts.POST_TYPE_FIELD).asText());

        return (AbstractPostCreateRequest) getIfValidOrThrow(requestClazz.cast(objectMapper.readValue(requestTree.toString(), requestClazz)));
    }

    private Class<?> postRequestFactory(String postType) { // FIXME 개선 필요
        switch (postType) {
            case KeeweConsts.ANNOUNCE_POST:
            case KeeweConsts.QUESTION_POST:
                return CommonPostCreateRequest.class;
            case KeeweConsts.VOTE_POST:
                return VotePostCreateRequest.class;
            default:
                throw new KeeweException(KeeweRtnConsts.ERR506);
        }

    }

    private Object getIfValidOrThrow(Object request){
        Set<? extends ConstraintViolation<?>> validate = validator.validate(request);
        if(!CollectionUtils.isEmpty(validate)) {
            throw new ConstraintViolationException(validate);
        }
        return request;
    }
}
