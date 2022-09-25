import {ParsedUrlQuery} from "querystring";
import {Filter, Pageable} from "./ApiClient";

export function buildPageable(query: ParsedUrlQuery): Pageable {
    return {
        page: getFirstArrayEntryOrDefault(query, "page", "0"),
        size: getFirstArrayEntryOrDefault(query, "size", "20"),
        sort: getFirstArrayEntryOrDefault(query, "sort", ""),
    }
}

export function buildFilter(query: ParsedUrlQuery): Filter {
    const map = new Map()
    Object
        .keys(query)
        .filter((key) => key != "page" && key != "size" && key != "filter")
        .forEach((value) => {
            map.set(value, query[value])
        })
    return map
}

function getFirstArrayEntryOrDefault(val: any, key: string, defaultData: string): string {
    if(!val) {
        return defaultData
    }

    if(!(key in val)) {
        return defaultData
    }

    const data = val[key]
    if(!Array.isArray(data)) {
        if(typeof data === 'string')
            return data
        else {
            return defaultData
        }
    }

    return data[0]
}

export function buildQueryFromText(text: string): string {
    const parts = text.split("=")
    const queryParts = []

    if(parts.length % 2 != 0) {
        throw new Error("Query parts length is not even")
    }

    for(let i = 0; i < parts.length; i += 2) {
        queryParts.push(`${encodeURIComponent(parts[i])}=${encodeURIComponent(parts[i+1])}`)
    }

    return queryParts.join("&")
}