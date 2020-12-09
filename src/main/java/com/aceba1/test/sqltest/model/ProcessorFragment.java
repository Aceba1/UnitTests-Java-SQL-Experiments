package com.aceba1.test.sqltest.model;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * (${...})
 */
public class ProcessorFragment {
  /**
   * Types:
   * t - CURRENT_TIMESTAMP
   * a - (A + B)
   * s - (A - B)
   * d - (A / B)
   * m - (A * B)
   * e - (A ^ B)
   * v - A // A - Value, B - Null
   * c - B.A // A - Column Name, B - Table Name
   * (X) count - COUNT(...) << Expects GROUP
   * (X) average - AVG(...) << Expects INT
   * (X) sum - SUM() << Expects INT
   */
  public String type;

  public JsonNode a, b;

  public static String toString(JsonNode fragment, String salt) {
    return toString( // Avoid casting to this type, grab what we already know
      fragment.get("type").asText(),
      fragment.get("a"),
      fragment.get("b"),
      salt);
  }

  public static String toString(String type, JsonNode a, JsonNode b, String salt) {
    return switch (type) {
      case "t" -> "CURRENT_TIMESTAMP";
      case "e" -> "(" + toString(a, salt) + "^" + toString(b, salt) + ")";
      case "m" -> "(" + toString(a, salt) + "*" + toString(b, salt) + ")";
      case "d" -> "(" + toString(a, salt) + "/" + toString(b, salt) + ")";
      case "a" -> "(" + toString(a, salt) + "+" + toString(b, salt) + ")";
      case "s" -> "(" + toString(a, salt) + "-" + toString(b, salt) + ")";
      case "v" -> a.asText();
      case "c" -> ColumnProcessor.verifyWrappedTable(b.asText(), salt)  + "." + a.asText(); // This is what salt is for
      default -> "/* UNKNOWN type (" + type + ") */"; // Sanitizing JSON beforehand should keep this safe
    };
  }

  public String toString(String privateSalt) {
    return toString(type, a, b, privateSalt);
  }

  @Override
  @Deprecated
  public String toString() {
    return "ProcessorFragment {\n  type:" + type +
      "\n  a:" + a.toString() +
      "\n  b:" + b.toString() + " }";
  }
}
