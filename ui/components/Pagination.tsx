import {PageableResult} from "../lib/ApiClient";
import Link from "next/link";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faAngleLeft, faAngleRight, faAnglesLeft, faAnglesRight} from "@fortawesome/free-solid-svg-icons";

export type PaginationProp<T> = {
    result: PageableResult<T>,
    baseUri: string,
    children: any,
}

function hasPrevious<T>(result: PageableResult<T>) {
    return result.page > 0
}

function hasNext<T>(result: PageableResult<T>) {
    return ((result.page + 1) * result.size) < result.total
}

function buildNextLink<T>(result: PageableResult<T>, baseUri: string) {
    return `${baseUri}?page=${result.page + 1}&size=${result.size}&sort=${result.sort}`
}

function buildPreviousLink<T>(result: PageableResult<T>, baseUri: string) {
    return `${baseUri}?page=${result.page - 1}&size=${result.size}&sort=${result.sort}`
}

export default function Pagination<T>({ result, baseUri, children }: PaginationProp<T>) {
    const previous = hasPrevious(result)
    const next = hasNext(result)
    const previousClassName = "page-item" + (previous ? "" : " disabled")
    const nextClassName = "page-item" + (next ? "" : " disabled")

    return (
        <div>
            {children}
            <nav className="d-flex justify-content-center pt-2">
                <ul className="pagination">
                    <li className="page-item">
                        <Link href={`${baseUri}?page=0&size=${result.size}&sort=${result.sort}`}>
                            <a className="page-link">
                                <FontAwesomeIcon icon={faAnglesLeft} />
                                First
                            </a>
                        </Link>
                    </li>
                    <li className={previousClassName}>
                        {previous ?
                            <Link href={buildPreviousLink(result, baseUri)}>
                                <a className="page-link">
                                    <FontAwesomeIcon icon={faAngleLeft} />
                                    Previous
                                </a>
                            </Link>
                            :
                            <span className="page-link">
                                <FontAwesomeIcon icon={faAngleLeft} />
                                Previous
                            </span>
                        }
                    </li>
                    <li className="page-item active">
                        <span className="page-link">{result.page}</span>
                    </li>
                    <li className={nextClassName}>
                        {next ?
                            <Link href={buildNextLink(result, baseUri)}>
                                <a className="page-link">
                                    Next
                                    <FontAwesomeIcon icon={faAngleRight} />
                                </a>
                            </Link>
                            :
                            <span className="page-link">
                                Next
                                <FontAwesomeIcon icon={faAngleRight} />
                            </span>
                        }
                    </li>
                    <li className="page-item">
                        <Link href={`${baseUri}?page=${Math.floor(result.total / result.size)}&size=${result.size}&sort=${result.sort}`}>
                            <a className="page-link">
                                Last
                                <FontAwesomeIcon icon={faAnglesRight} />
                            </a>
                        </Link>
                    </li>
                </ul>
            </nav>
        </div>
    )
}