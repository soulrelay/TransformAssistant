package com.me.compile;

import com.google.auto.service.AutoService;
import com.me.anotation.AST;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;


import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)//自动生成 javax.annotation.processing.IProcessor 文件
@SupportedSourceVersion(SourceVersion.RELEASE_8)//java版本支持
@SupportedAnnotationTypes({"com.me.anotation.AST"})//注意替换成你自己的注解名
public class ASTProcessor extends AbstractProcessor {
    private Trees trees;
    private HashMap<String, List<JCTree.JCStatement>> mCodes = new HashMap<>();
    private HashMap<String, CodeItem> mTargets = new HashMap<>();
    private HashMap<String, PriorityQueue<CodeItem>> mSources = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        trees = Trees.instance(env);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        if (!roundEnv.processingOver()) {
            roundEnv.getRootElements().stream()
                    .filter(it -> it.getKind() == ElementKind.CLASS)
                    .forEach(it -> ((JCTree) trees.getTree(it)).accept(new AOPTreeTranslator()));
        } else {
            mTargets.entrySet().stream()
                    .filter(it -> it.getValue().level == AST.LEVEL.AFTER)
                    .forEach(it -> mSources.get(it.getKey()).forEach(node
                            -> it.getValue().med.body.stats
                            = it.getValue().med.body.stats.appendList(node.med.body.stats)));

            mTargets.entrySet().stream()
                    .filter(it -> it.getValue().level == AST.LEVEL.BEFORE)
                    .forEach(it -> mSources.get(it.getKey())
                            .forEach(node -> {
                                List<JCTree.JCStatement> list;
                                if (mCodes.get(it.getKey()) != null)
                                    list = mCodes.get(it.getKey()).appendList(node.med.body.stats);
                                else list = List.from(node.med.body.stats);
                                mCodes.put(it.getKey(), list);
                            }));
            mCodes.forEach((key, value) ->
                    mTargets.get(key).med.body.stats = mTargets.get(key).med.body.stats.prependList(value));
        }
        return false;
    }

    class CodeItem {
        int level;//优先级
        JCTree.JCMethodDecl med;//方法体

        CodeItem(int levelType, JCTree.JCMethodDecl jcMethodDecl) {
            this.level = levelType;
            this.med = jcMethodDecl;
        }
    }

    private class AOPTreeTranslator extends TreeTranslator {
        @Override
        public void visitMethodDef(JCTree.JCMethodDecl jcMethod) {
            super.visitMethodDef(jcMethod);
            if (jcMethod.getModifiers().annotations.toString().contains(AST.class.getSimpleName())) {
                String type = getValue(jcMethod, 0);
                String id = getValue(jcMethod, 1);
                String levelType = getValue(jcMethod, 2);
                if (type.equals("AST.TYPE.SOURCE")) {

                    int level = Integer.parseInt(levelType);
                    if (mSources.get(id) != null) {
                        mSources.get(id).add(new CodeItem(level, jcMethod));
                    } else {
                        PriorityQueue<CodeItem> list = new PriorityQueue<>(Comparator.comparingInt(p -> p.level));
                        list.add(new CodeItem(level, jcMethod));
                        mSources.put(id, list);
                    }
                } else {
                    int level = levelType.equals("AST.LEVEL.BEFORE") ? AST.LEVEL.BEFORE : AST.LEVEL.AFTER;
                    mTargets.put(id, new CodeItem( level, jcMethod));
                }
            }
        }
    }

    private String getValue(JCTree.JCMethodDecl jcMethod, int index) {
        return jcMethod.getModifiers().annotations.get(0).getArguments().get(index).toString().split("=")[1].trim();
    }
}

