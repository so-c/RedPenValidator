package soc.com.github.redpenvalidator

import org.junit.Test
import static org.junit.Assert.*
import static org.hamcrest.CoreMatchers.*

import cc.redpen.RedPen;
import cc.redpen.validator.JavaScriptValidator

import cc.redpen.config.ValidatorConfiguration
import cc.redpen.config.Configuration

import cc.redpen.parser.DocumentParser
import cc.redpen.parser.SentenceExtractor;

/**
 * Precondition: add following jars to ClassPath from REDPEN_HOME\lib:
 *               run in home of this repository.
 */
class UnsuggestedKanjiTest {
  def vc = new ValidatorConfiguration("JavaScript")
              .addAttribute("script-path", System.getProperty("user.dir") + /\src/)
  def conf = new Configuration.ConfigurationBuilder()
                              .addValidatorConfig(vc)
                              .setLanguage("ja")
                              .build()

  @Test
  void test_javascript_validators_are_loaded() {
    // setup
    def sut = new JavaScriptValidator()
    sut.preInit(vc, null)
    
    // verify
    assertThat sut.scripts.size(), is(1)
  }
  
  @Test 
  void test_check_��() {
    // setup
    def parser = DocumentParser.PLAIN
    def doc = parser.parse("�m��Ȃ���������B", 
                           new SentenceExtractor(conf.getSymbolTable()),
                           conf.getTokenizer())
     
    // exercise
    def errors = (new RedPen(conf)).validate([doc]).get(doc)
     
    // verify
    assertThat errors.size(), is(1)
    assertThat errors.get(0).getMessage(), is("[unsuggested-kanji.js] �������Ȃ��������u���v�Ŏg���Ă��܂��B")

  }
}