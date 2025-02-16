package com.example.marketplace_crm.Service.Impl;

import com.example.marketplace_crm.Model.Tag;
import com.example.marketplace_crm.Repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagService extends BaseServiceImpl<Tag, String> {
    private TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        super(tagRepository);
        this.tagRepository = tagRepository;
    }


}
