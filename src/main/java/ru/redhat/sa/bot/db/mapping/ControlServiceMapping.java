package ru.redhat.sa.bot.db.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import ru.redhat.sa.bot.db.Alias;
import ru.redhat.sa.bot.db.Phrase;

public interface ControlServiceMapping {
	
	@Update("CREATE TABLE IF NOT EXISTS T_PHRASES(PHRASE_NAME VARCHAR(50), PHRASE_TEXT VARCHAR(255), PHRASE_TYPE VARCHAR(30)); "
			+ "CREATE TABLE IF NOT EXISTS T_PHRASE_ALIASES(PHRASE_NAME VARCHAR(50), ALIAS_NAME VARCHAR(50));")
	void createTables10();
	
	@Update("CREATE TABLE IF NOT EXISTS T_ENTITY(uuid UUID PRIMARY KEY, date_time TIMESTAMP, version VARCHAR(64), status VARCHAR(64)," +
			"classifier VARCHAR(64), color VARCHAR(64), morphology VARCHAR(64), user_key VARCHAR(64), user_key_reference VARCHAR(64)," +
			"body VARCHAR(64000));" +
			"CREATE TABLE IF NOT EXISTS T_LOG(uuid UUID PRIMARY KEY, date_time TIMESTAMP, user_name VARCHAR(64), body VARCHAR(64000));")
	void createTables20();
	
	@Insert("merge into T_PHRASES(PHRASE_NAME, PHRASE_TEXT, PHRASE_TYPE) KEY(PHRASE_NAME) values (#{phraseName}, #{phraseText}, #{phraseType})")
	void insertPhrase(Phrase phrase);
	
	@Insert("merge into T_PHRASE_ALIASES(PHRASE_NAME, ALIAS_NAME) KEY(ALIAS_NAME, PHRASE_NAME) values (#{phraseName}, #{aliasName})")
	void insertAlias(Alias alias);
	
	@Results({
        @Result(property = "phraseName", column = "PHRASE_NAME"),
        @Result(property = "aliasName", column = "ALIAS_NAME")
    })
	@Select("select PHRASE_NAME, ALIAS_NAME from T_PHRASE_ALIASES order by PHRASE_NAME, ALIAS_NAME")
	List<Alias> selectAliases();
	
	@Results({
        @Result(property = "phraseName", column = "PHRASE_NAME"),
        @Result(property = "phraseText", column = "PHRASE_TEXT"),
        @Result(property = "phraseType", column = "PHRASE_TYPE")
    })
	@Select("select PHRASE_NAME, PHRASE_TEXT, PHRASE_TYPE from T_PHRASES")
	List<Phrase> selectPhrases();
	
	@Delete("delete from T_PHRASES where PHRASE_NAME = #{phraseName} and PHRASE_TYPE = 'MEM'")
	void deleteMem(String phraseName);
	
	@Delete("delete from T_PHRASE_ALIASES where ALIAS_NAME = #{aliasName}")
	void deleteAlias(String aliasName);
}