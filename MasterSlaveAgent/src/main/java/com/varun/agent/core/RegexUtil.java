package com.varun.agent.core;

public class RegexUtil {

  /**
   * Converts given wild card to java regex equivalent which can be used in java
   * Patterns. This method is enhanced from
   * com.sun.enterprise.util.RegexUtil.globToRegex method from Glassfish library
   * as ee dont want to have dependency on glassfish module at present
   * 
   * @param wildCard
   *          wildCard pattern to be converted to java regex
   * @return RegEx equivalent to provided wild card. Returns null, if given
   *         wildCard is null
   * 
   */
  public static String wildCardToRegEx(String wildCard) {
    if (wildCard == null || wildCard.isEmpty()) {
      return null;
    }
    wildCard = wildCard.trim();
    int strLen = wildCard.length();
    StringBuilder sb = new StringBuilder(strLen);
    boolean escaping = false;
    int inCurlies = 0;
    for (char currentChar : wildCard.toCharArray()) {
      switch (currentChar) {
        case '*' :
          if (escaping)
            sb.append("\\*");
          else
            sb.append(".*");
          escaping = false;
          break;
        case '?' :
          if (escaping)
            sb.append("\\?");
          else
            sb.append('.');
          escaping = false;
          break;
        case '.' :
        case '(' :
        case ')' :
        case '+' :
        case '|' :
        case '^' :
        case '$' :
        case '@' :
        case '%' :
          sb.append('\\');
          sb.append(currentChar);
          escaping = false;
          break;
        case '\\' :
          if (escaping) {
            sb.append("\\\\");
            escaping = false;
          } else
            escaping = true;
          break;
        case '{' :
          if (escaping) {
            sb.append("\\{");
          } else {
            sb.append('(');
            inCurlies++;
          }
          escaping = false;
          break;
        case '}' :
          if (inCurlies > 0 && !escaping) {
            sb.append(')');
            inCurlies--;
          } else if (escaping)
            sb.append("\\}");
          else
            sb.append("}");
          escaping = false;
          break;
        case ',' :
          if (inCurlies > 0 && !escaping)
            sb.append('|');
          else if (escaping)
            sb.append("\\,");
          else
            sb.append(",");
          break;
        default :
          escaping = false;
          sb.append(currentChar);
      }
    }
    return sb.toString();
  }

}
