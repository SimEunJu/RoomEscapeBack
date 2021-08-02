package com.sej.escape.utils.geolocation;

import com.sej.escape.constants.AreaSection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Getter
public class AreaSectionUtil {

    public final List<AreaSectionNode> areaSectionTree;

    public AreaSectionUtil(){
        this.areaSectionTree = genAreaSectionTree(AreaSection.NATIONWIDE);
    }

    private List<AreaSectionNode> genAreaSectionTree(AreaSection parent){
        if(parent.getSubSections() == null) return null;

        return parent.getSubSections().stream()
                .map(item -> new AreaSectionNode( item.name(), item.getTitle(), genAreaSectionTree(item)) )
                .collect(Collectors.toList());
    }

    private List<String> getTitleFromAreaCode(List<AreaSectionNode> tree, double areaCode, List<String> areas){

        for (AreaSectionNode item : tree) {
            AreaSection areaSection = AreaSection.valueOf(item.getName());
            AreaSection.AreaCode itemAreaCode = areaSection.getAreaCode();

            double lower = itemAreaCode.getLower();
            double upper = itemAreaCode.getUpper();

            if(areaCode >= lower && areaCode < upper){
                areas.add(item.getName());
                return getTitleFromAreaCode(item.getChildren(), areaCode, areas);
            }
        }
        return areas;
    }

    private List<String> getTitleFromAreaCode(double areaCode, List<String> areas){
        return getTitleFromAreaCode(areaSectionTree, areaCode, areas);
    }

    public List<String> getTitleFromAreaCode(double areaCode){
        return getTitleFromAreaCode(areaCode, new ArrayList<>());
    }

    @Getter
    @AllArgsConstructor
    public class AreaSectionNode{
        private String name;
        private String title;
        private List<AreaSectionNode> children;
    }

}
