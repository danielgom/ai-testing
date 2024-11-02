package com.mna.aipj.mapper;

import com.mna.aipj.dto.MentionInformation;
import com.mna.aipj.external.brandwatch.BrandWatchMention;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MentionMapper {

    MentionMapper MAPPER = Mappers.getMapper(MentionMapper.class);

    @Mapping(target = "actor", ignore = true)
    @Mapping(target = "sentiment", ignore = true)
    @Mapping(target = "isClassified", ignore = true)
    @Mapping(target = "isImportant", ignore = true)
    @Mapping(target = "isAnalyzed", ignore = true)
    @Mapping(target = "actorProfession", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "topicImportance", ignore = true)
    @Mapping(target = "actorImportance", ignore = true)
    @Mapping(target = "title", source = "title")
    @Mapping(target = "snippet", source = "snippet")
    @Mapping(target = "domain", source = "domain")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "source", source = "contentSourceName")
    @Mapping(target = "date", source = "mentionDate")
    MentionInformation toMentionInformation(BrandWatchMention brandWatchMention);
}
